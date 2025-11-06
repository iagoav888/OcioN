import json
import secrets

from django.db.models import Q
from django.http import JsonResponse
from django.views.decorators.csrf import csrf_exempt

from MiOcioNApp.models import AppUser, Local


# ------------------------------------------------------------
# Endpoint: register_user
# Permite registrar un nuevo usuario en la aplicación.
# Recibe por POST un nombre de usuario y contraseña en JSON,
# comprueba que no exista ya y lo guarda en la base de datos.
# Devuelve un mensaje de éxito o un error si falla algo.
# ------------------------------------------------------------
@csrf_exempt
def register_user(request):
    if request.method != "POST":
        return JsonResponse({"error": "Método HTTP no soportado"}, status=405)

    try:
        data = json.loads(request.body)
        username = data.get("username")
        password = data.get("password")
    except json.JSONDecodeError:
        return JsonResponse({"error": "Solicitud inválida"}, status=400)

    if not username or not password:
        return JsonResponse({"error": "Faltan campos obligatorios"}, status=400)

    if AppUser.objects.filter(username=username).exists():
        return JsonResponse({"error": "El nombre de usuario ya existe"}, status=400)

    # TODO: En producción usar hash para la contraseña
    # from django.contrib.auth.hashers import make_password
    # password = make_password(password)

    AppUser.objects.create(username=username, password=password)
    return JsonResponse({"mensaje": "Usuario registrado correctamente"}, status=201)


# ------------------------------------------------------------
# Endpoint: session
# Gestiona el inicio de sesión de un usuario.
# Recibe por POST el nombre de usuario y contraseña en JSON,
# verifica los datos y genera un token de sesión si son correctos.
# Devuelve el token o un error si los datos no son válidos.
# ------------------------------------------------------------
@csrf_exempt
def session(request):
    if request.method != "POST":
        return JsonResponse({"error": "Método HTTP no soportado"}, status=405)

    try:
        data = json.loads(request.body)
        username = data.get("username")
        password = data.get("password")
    except json.JSONDecodeError:
        return JsonResponse({"error": "Solicitud inválida"}, status=400)

    user = AppUser.objects.filter(username=username).first()

    if user is None:
        return JsonResponse({"error": "Usuario no encontrado"}, status=404)

    # TODO: En producción usar check_password para contraseñas hasheadas
    # from django.contrib.auth.hashers import check_password
    # if not check_password(password, user.password):

    if user.password != password:
        return JsonResponse({"error": "Contraseña incorrecta"}, status=403)

    # Generar token de sesión único
    token = secrets.token_hex(32)
    user.tokenSessions = token
    user.save()

    return JsonResponse({"token": token}, status=201)


# ------------------------------------------------------------
# Endpoint: list_locales
# Devuelve un listado con todos los locales registrados.
# Se accede mediante GET y devuelve los campos principales
# en formato JSON.
# ------------------------------------------------------------
def list_locales(request):
    if request.method != "GET":
        return JsonResponse({"error": "Método HTTP no soportado"}, status=405)

    locales = Local.objects.all().values(
        "id",
        "nombre",
        "descripcion",
        "ubicacion",
        "imagen_url",
        "playlist_url"
    )
    return JsonResponse(list(locales), safe=False)


# ------------------------------------------------------------
# Endpoint: local_details
# Muestra los detalles de un local específico.
# Se accede mediante GET pasando el id del local en la URL.
# Devuelve la información completa del local o error si no existe.
# ------------------------------------------------------------
def local_details(request, local_id):
    if request.method != "GET":
        return JsonResponse({"error": "Método HTTP no soportado"}, status=405)

    try:
        local = Local.objects.get(id=local_id)
        data = {
            "id": local.id,
            "nombre": local.nombre,
            "descripcion": local.descripcion,
            "ubicacion": local.ubicacion,
            "imagen_url": local.imagen_url,
            "playlist_url": local.playlist_url
        }

        return JsonResponse(data)

    except Local.DoesNotExist:
        return JsonResponse({"error": "Local no encontrado"}, status=404)


# ------------------------------------------------------------
# Endpoint: search_locales
# Permite buscar locales por nombre o descripción.
# Se usa con el método GET y el parámetro 'query' en la URL.
# Devuelve una lista de locales que coinciden con la búsqueda.
# ------------------------------------------------------------
def search_locales(request):
    if request.method != "GET":
        return JsonResponse({"error": "Método HTTP no soportado"}, status=405)

    query = request.GET.get("query", "")
    locales = Local.objects.filter(
        Q(nombre__icontains=query) | Q(descripcion__icontains=query)
    )
    data = list(locales.values("id", "nombre", "ubicacion", "imagen_url"))
    return JsonResponse(data, safe=False)