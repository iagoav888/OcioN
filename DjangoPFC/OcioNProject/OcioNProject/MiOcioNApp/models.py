from django.core.validators import MinValueValidator, MaxValueValidator
from django.db import models


class AppUser(models.Model):
    """
    Modelo que representa un usuario de la aplicación.
    Almacena username, password y token de sesión.
    """
    username = models.CharField(max_length=50, unique=True)
    password = models.CharField(max_length=130)
    tokenSessions = models.CharField(max_length=64, blank=True, null=True)

    def __str__(self):
        return self.username

    class Meta:
        verbose_name = "Usuario"
        verbose_name_plural = "Usuarios"


class Local(models.Model):
    """
    Modelo que representa un local de ocio nocturno.
    Incluye información básica y URLs de imagen y playlist.
    """
    nombre = models.CharField(max_length=100)
    descripcion = models.TextField(blank=True, null=True)
    ubicacion = models.CharField(max_length=250)
    tipo = models.CharField(max_length=50, default='Local')
    imagen_url = models.URLField(blank=True, null=True)
    playlist_url = models.URLField(blank=True, null=True)

    def __str__(self):
        return self.nombre

    class Meta:
        verbose_name = "Local"
        verbose_name_plural = "Locales"


class Review(models.Model):
    """
    Modelo que representa una reseña de un usuario sobre un local.
    Incluye puntuación del 1 al 5 y contenido de texto.
    """
    user = models.ForeignKey(AppUser, on_delete=models.CASCADE, related_name="reviews")
    local = models.ForeignKey(Local, on_delete=models.CASCADE, related_name="reviews")
    contenido = models.TextField()
    puntuacion = models.IntegerField(
        validators=[MinValueValidator(1), MaxValueValidator(5)]
    )
    fecha = models.DateTimeField(auto_now_add=True)

    def __str__(self):
        return f"Reseña de {self.user.username} en {self.local.nombre}"

    class Meta:
        verbose_name = "Reseña"
        verbose_name_plural = "Reseñas"
        ordering = ['-fecha']  # Más recientes primero


class Evento(models.Model):
    """
    Modelo que representa un evento o fiesta temática en un local.
    Almacena título, descripción y fecha del evento.
    """
    local = models.ForeignKey(Local, on_delete=models.CASCADE, related_name="eventos")
    titulo = models.CharField(max_length=100)
    descripcion = models.TextField(blank=True, null=True)
    fecha = models.DateTimeField()

    def __str__(self):
        return f"{self.titulo} ({self.local.nombre})"

    class Meta:
        verbose_name = "Evento"
        verbose_name_plural = "Eventos"
        ordering = ['fecha']  # Próximos eventos primero