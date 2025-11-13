import json
import secrets
from datetime import datetime

from django.db.models import Q
from django.http import JsonResponse
from django.views.decorators.csrf import csrf_exempt

from MiOcioNApp.models import AppUser, Local, Review


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
        "tipo",
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
            "tipo": local.tipo,
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
    data = list(locales.values("id", "nombre", "ubicacion", "tipo", "imagen_url"))
    return JsonResponse(data, safe=False)


# ------------------------------------------------------------
# Endpoint: reviews
# Gestiona las reseñas de los locales.
# GET: Lista todas las reseñas de un local específico
# POST: Crea una nueva reseña (requiere autenticación con token)
# ------------------------------------------------------------
@csrf_exempt
def reviews(request, local_id):
    # GET: Listar reseñas de un local
    if request.method == "GET":
        try:
            local = Local.objects.get(id=local_id)
        except Local.DoesNotExist:
            return JsonResponse({"error": "Local no encontrado"}, status=404)

        # Obtener todas las reseñas del local ordenadas por fecha (más recientes primero)
        reseñas = Review.objects.filter(local=local).order_by('-fecha')

        data = []
        for review in reseñas:
            data.append({
                "id": review.id,
                "username": review.usuario.username,
                "contenido": review.contenido,
                "puntuacion": review.puntuacion,
                "fecha": review.fecha.strftime("%Y-%m-%d %H:%M:%S")
            })

        return JsonResponse(data, safe=False)

    # POST: Crear una nueva reseña
    elif request.method == "POST":
        try:
            data = json.loads(request.body)
            token = data.get("token")
            contenido = data.get("contenido")
            puntuacion = data.get("puntuacion")
        except json.JSONDecodeError:
            return JsonResponse({"error": "Solicitud inválida"}, status=400)

        # Validar campos obligatorios
        if not token or not contenido or puntuacion is None:
            return JsonResponse({"error": "Faltan campos obligatorios (token, contenido, puntuacion)"}, status=400)

        # Verificar autenticación
        user = AppUser.objects.filter(tokenSessions=token).first()
        if user is None:
            return JsonResponse({"error": "Token inválido o sesión expirada"}, status=401)

        # Verificar que el local existe
        try:
            local = Local.objects.get(id=local_id)
        except Local.DoesNotExist:
            return JsonResponse({"error": "Local no encontrado"}, status=404)

        # Validar puntuación (debe estar entre 1 y 5)
        if not isinstance(puntuacion, int) or puntuacion < 1 or puntuacion > 5:
            return JsonResponse({"error": "La puntuación debe ser un número entre 1 y 5"}, status=400)

        # Crear la reseña
        review = Review.objects.create(
            usuario=user,
            local=local,
            contenido=contenido,
            puntuacion=puntuacion
        )

        return JsonResponse({
            "mensaje": "Reseña creada correctamente",
            "review": {
                "id": review.id,
                "username": user.username,
                "contenido": review.contenido,
                "puntuacion": review.puntuacion,
                "fecha": review.fecha.strftime("%Y-%m-%d %H:%M:%S")
            }
        }, status=201)

    else:
        return JsonResponse({"error": "Método HTTP no soportado"}, status=405)


# ------------------------------------------------------------
# Endpoint: user_reviews
# Devuelve todas las reseñas escritas por el usuario autenticado.
# Requiere token de autenticación.
# ------------------------------------------------------------
def user_reviews(request):
    if request.method != "GET":
        return JsonResponse({"error": "Método HTTP no soportado"}, status=405)

    # Obtener token desde parámetros GET
    token = request.GET.get("token")

    if not token:
        return JsonResponse({"error": "Token requerido"}, status=401)

    # Verificar autenticación
    user = AppUser.objects.filter(tokenSessions=token).first()
    if user is None:
        return JsonResponse({"error": "Token inválido o sesión expirada"}, status=401)

    # Obtener reseñas del usuario
    reseñas = Review.objects.filter(usuario=user).order_by('-fecha')

    data = []
    for review in reseñas:
        data.append({
            "id": review.id,
            "local_nombre": review.local.nombre,
            "local_id": review.local.id,
            "contenido": review.contenido,
            "puntuacion": review.puntuacion,
            "fecha": review.fecha.strftime("%Y-%m-%d %H:%M:%S")
        })

    return JsonResponse(data, safe=False)