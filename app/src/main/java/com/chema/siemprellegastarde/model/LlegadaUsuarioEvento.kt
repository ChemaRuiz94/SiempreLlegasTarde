package com.chema.siemprellegastarde.model

import java.io.Serializable

data class LlegadaUsuarioEvento(
    var nombreEvento: String,
    var emailUser: String,
    var horaLlegada: String
    ) : Serializable
