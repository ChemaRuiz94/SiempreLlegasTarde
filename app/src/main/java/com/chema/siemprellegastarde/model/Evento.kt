package com.chema.siemprellegastarde.model

import java.io.Serializable

data class Evento(
    var nombreEvento: String?,
    var fecha: String?,
    var hora: String?,
    var ubicacion: String?,
    var emailAsistentes: ArrayList<String>?) : Serializable
