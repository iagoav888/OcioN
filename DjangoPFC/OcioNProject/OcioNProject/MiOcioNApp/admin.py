from django.contrib import admin

from .models import AppUser, Local, Review, Evento

admin.site.register(AppUser)
admin.site.register(Local)
admin.site.register(Review)
admin.site.register(Evento)