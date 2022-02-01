package com.chema.siemprellegastarde.utils

import com.firebase.ui.auth.data.model.User
import com.google.android.gms.maps.model.LatLng

object VaraiblesComunes {
    var marcadorActual : LatLng = LatLng(-33.852, 151.211)
    var usuariosEventoActual: ArrayList<String> = ArrayList<String>()
    var usuarioActual: User? = null
    var emailUsuarioActual: String? = null
}