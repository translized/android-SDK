package com.translized.translized_ota.model

/**
 * TODO: Add type description!
 *
 * Created by nikolatomovic on 7.1.22..
 * Copyright © 2022 aktiia SA. All rights reserved.
 */
data class RequestCheckOta(val projectId: String, val otaToken: String, val appVersion: String, val currentOtaReleaseId: String, val userId: String, val platform: String)
