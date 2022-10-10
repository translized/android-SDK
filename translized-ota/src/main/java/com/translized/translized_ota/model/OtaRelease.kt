package com.translized.translized_ota.model

data class OtaRelease(val objectId: String?, val files: Array<OtaFile>?) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as OtaRelease

        if (objectId != other.objectId) return false
        if (files != null) {
            if (other.files == null) return false
            if (!files.contentEquals(other.files)) return false
        } else if (other.files != null) return false

        return true
    }

    override fun hashCode(): Int {
        var result = objectId?.hashCode() ?: 0
        result = 31 * result + (files?.contentHashCode() ?: 0)
        return result
    }
}
