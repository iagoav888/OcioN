import json
import secrets

from django.db.models import Q
from django.http import JsonResponse
from django.views.decorators.csrf import csrf_exempt

from MiOcioNApp.models import AppUser, Local


@csrf_exempt
def register_user(request):
    if request.method != "POST":
        return JsonResponse({"error": "Método HTTP no soportado"}, status=405)

    try:
        data = json.loads(request.body)
        username = data.get("username")
        password = data.get("password")
    except:
        return JsonResponse({"error": "Solicitud inválida"}, status=400)

    if not username or not password:
        return JsonResponse({"error": "Faltan campos obligatorios"}, status=400)

    if AppUser.objects.filter(username=username).exists():
        return JsonResponse({"error": "El nombre de usuario ya existe"}, status=400)

    AppUser.objects.create(username=username, password=password)
    return JsonResponse({"mensaje":"Usuario registrado correctamente"}, status=201)





@csrf_exempt
def session(request):
    if request.method != "POST":
        return JsonResponse({"error": "Método HTTP no soportado"}, status=405)

    try:
        data = json.loads(request.body)
        username = data.get("username")
        password = data.get("password")

    except:
        return JsonResponse({"error": "Solicitud inválida"}, status=400)

    user = AppUser.objects.filter(username=username).first()

    if user is None:
        return JsonResponse({"error": "Usuario no encontrado"}, status=404)

    if user.password != password:
        return JsonResponse({"error": "Contraseña incorrecta"}, status=403)


    token = secrets.token_hex(32)
    user.tokenSessions = token
    user.save()

    return JsonResponse({"token": token}, status=201)






def list_locales(request):
    if request.method != "GET":
        return JsonResponse({"error": "Método HTTP no soportado"}, status=405)

    locales = Local.objects.all().values("id", "nombre", "ubicacion", "imagen_url")
    return JsonResponse(list(locales), safe=False)


def local_details(request, local_id):
    if request.method != "GET":
        return JsonResponse({"error":"Método HTTP no soportado"}, status=405)

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
        return  JsonResponse({"error": "Local no encontrado"}, status=404)



def search_locales(request):
    if request.method != "GET":
        return JsonResponse({"error": "Método HTTP no soportado"}, status=405)

    query = request.GET.get("query", "")
    locales = Local.objects.filter(Q(nombre__icontains=query) | Q(descripcion__icontains=query))
    data = list(locales.values("id", "nombre", "ubicacion", "imagen_url"))
    return JsonResponse(data, safe=False)
