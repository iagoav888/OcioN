import json
import secrets

from django.http import JsonResponse
from django.views.decorators.csrf import csrf_exempt

from MiOcioNApp.models import AppUser


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

