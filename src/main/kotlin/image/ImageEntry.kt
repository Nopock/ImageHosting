package me.nopox.image.image

data class ImageEntry(var authorId: String?, var imageId: String, var image: ByteArray) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ImageEntry

        if (authorId != other.authorId) return false
        if (imageId != other.imageId) return false
        if (!image.contentEquals(other.image)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = authorId.hashCode()
        result = 31 * result + imageId.hashCode()
        result = 31 * result + image.contentHashCode()
        return result
    }
}
