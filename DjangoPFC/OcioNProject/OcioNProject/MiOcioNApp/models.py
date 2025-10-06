from django.core.validators import MinValueValidator, MaxValueValidator
from django.db import models





class AppUser(models.Model):
    username = models.CharField(max_length=50, unique= True)
    password = models.CharField(max_length=130)
    tokenSessions =  models.CharField(max_length=64, blank=True, null=True)

    def __str__(self):
        return self.username


class Local(models.Model):
    nombre = models.CharField(max_length=100)
    descripcion = models.TextField(blank=True, null=True)
    ubicacion = models.CharField(max_length=250)
    imagen_url = models.URLField(blank=True, null=True)
    playlist_url = models.URLField(blank=True, null=True)

    def __str__(self):
        return self.nombre


class Review(models.Model):
    user = models.ForeignKey(AppUser, on_delete=models.CASCADE, related_name="reviews")
    local = models.ForeignKey(Local, on_delete=models.CASCADE, related_name="reviews")
    contenido = models.TextField()
    puntuacion =models.IntegerField(
        validators=[MinValueValidator(1), MaxValueValidator(5)]
    )
    fecha = models.DateTimeField(auto_now_add=True)


    def __str__(self):
        return f"Reseña de {self.user.username} en {self.local.nombre}"


class Evento(models.Model):
    local = models.ForeignKey(Local, on_delete=models.CASCADE, related_name="eventos")
    titulo = models.CharField(max_length=100)
    descripcion = models.TextField(blank=True, null=True)
    fecha = models.DateTimeField()

    def __str__(self):
        return f"{self.titulo} ({self.local.nombre})"