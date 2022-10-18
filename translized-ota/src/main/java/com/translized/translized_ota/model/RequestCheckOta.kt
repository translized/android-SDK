package com.translized.translized_ota.model

data class RequestCheckOta(val projectId: String, val otaToken: String, val appVersion: String, val currentOtaReleaseId: String, val userId: String, val platform: String, val lastCheckDate: String)
