package test.app.everything.features.list.data

import test.app.everything.core.mapper.Mapper
import test.app.everything.features.list.data.remote.response.ArticleDataDto
import test.app.everything.features.list.domain.ArticleData
import java.util.UUID
import javax.inject.Inject

/**
 * Mapper class responsible for mapping ArticleDataDto objects to Articles objects.
 */
class ArticlesMapper @Inject constructor() : Mapper<ArticleDataDto, ArticleData> {

    /**
     * Method to map a ArticleDataDto object to a Articles object.
     *
     * @param model The ArticleDataDto object to be mapped.
     * @return The mapped Articles object.
     */
    override fun map(model: ArticleDataDto): ArticleData {
        // Extract data from ArticleDataDto and create a Articles object
        with(model) {
            return ArticleData(
                id = UUID.randomUUID().toString(),
                author = author,
                title = title,
                content = content,
                description = description,
                url = url,
                urlToImage = urlToImage,
                publishedAt = publishedAt,
                source = source
            )
        }
    }
}
