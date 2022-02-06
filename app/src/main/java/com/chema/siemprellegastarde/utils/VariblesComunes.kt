package com.chema.siemprellegastarde.utils

import com.chema.siemprellegastarde.model.Evento
import com.firebase.ui.auth.data.model.User
import com.google.android.gms.maps.model.LatLng

object VariblesComunes {
    var marcadorActual : LatLng = LatLng(-33.852, 151.211)
    var latEventoActual: String? = null
    var lonEventoActual: String? = null
    var usuariosEventoActual: ArrayList<String> = ArrayList<String>()
    var usuarioActual: User? = null
    var eventoActual: Evento? = null
    var emailUsuarioActual: String? = null
}