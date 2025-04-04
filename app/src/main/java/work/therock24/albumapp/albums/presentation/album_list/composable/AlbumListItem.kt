package work.therock24.albumapp.albums.presentation.album_list.composable

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import work.therock24.albumapp.R
import work.therock24.albumapp.albums.presentation.models.AlbumUiModel
import work.therock24.albumapp.ui.theme.DevicePreview
import work.therock24.albumapp.ui.theme.Dimensions


/**
 * A composable function that displays an album item in a list.
 * It shows the album image, ID, album ID, and title.
 *
 * @param item The [AlbumUiModel] representing the album to be displayed.
 */
@Composable
fun AlbumItem(item: AlbumUiModel) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .padding(top = Dimensions.PaddingMedium)
            .width(Dimensions.AlbumItemWidth)
    ) {
        AsyncImage(
            model = item.url,
            error = painterResource(id = R.drawable.ic_placeholder_image),
            contentDescription = stringResource(R.string.albums_image_content_description),
            modifier = Modifier
                .width(Dimensions.AlbumItemImageWidth)
        )

        Spacer(modifier = Modifier.height(Dimensions.PaddingSmall))

        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .width(Dimensions.AlbumItemTitleWidth)
                .align(Alignment.CenterHorizontally)
                .padding(horizontal = 4.dp)
        ) {
            Text(
                text = "ID: ${item.id}",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.primary
            )
            Text(
                text = "Album: ${item.albumId}",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.primary
            )
        }

        Spacer(modifier = Modifier.height(4.dp))

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "Album Title",
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(2.dp))

            Text(
                text = item.title,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .width(Dimensions.AlbumItemTitleWidth)
            )
        }
    }
}

@DevicePreview
@Composable
fun PreviewAlbumItem() {
    val mockAlbum = AlbumUiModel(
        albumId = 1,
        id = 42,
        title = "repudiandae inventore architecto placeat quia eveniet eum",
        url = "https://via.placeholder.com/600/92c952",
        thumbnailUrl = "https://via.placeholder.com/150/92c952"
    )

    AlbumItem(item = mockAlbum)
}
