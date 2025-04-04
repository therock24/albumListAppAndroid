package work.therock24.albumapp.albums.data.mapper

import work.therock24.albumapp.albums.data.local.entity.AlbumEntity
import work.therock24.albumapp.albums.data.remote.model.AlbumDto
import work.therock24.albumapp.albums.domain.model.Album


fun Album.toAlbumEntity(): AlbumEntity {
    return AlbumEntity(
        id = this.id,
        albumId = this.albumId,
        title = this.title,
        url = this.url,
        thumbnailUrl = this.thumbnailUrl
    )
}

fun AlbumDto.toAlbum(): Album {
    return Album(
        id = this.id,
        albumId = this.albumId,
        title = this.title,
        url = this.url,
        thumbnailUrl = this.thumbnailUrl
    )
}

fun AlbumEntity.toAlbum(): Album {
    return Album(
        id = this.id,
        albumId = this.albumId,
        title = this.title,
        url = this.url,
        thumbnailUrl = this.thumbnailUrl
    )
}