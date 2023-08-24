package com.tfm.musiccommunityapp.ui.di

import android.content.Context
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.bumptech.glide.GlideBuilder
import com.bumptech.glide.Registry
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.integration.okhttp3.OkHttpUrlLoader
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.module.AppGlideModule
import com.bumptech.glide.request.RequestOptions
import com.tfm.musiccommunityapp.R
import com.tfm.musiccommunityapp.data.network.di.NetworkDatasourceModule
import okhttp3.OkHttpClient
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.qualifier.named
import java.io.InputStream

@GlideModule
class MusicCommunityGlideModule: AppGlideModule(), KoinComponent {

    private val client: OkHttpClient by inject(named(NetworkDatasourceModule.SERVICE_OKHTTP_CLIENT))

    override fun registerComponents(context: Context, glide: Glide, registry: Registry) {
        super.registerComponents(context, glide, registry)
        registry.replace(
            GlideUrl::class.java,
            InputStream::class.java,
            OkHttpUrlLoader.Factory(client)
        )
    }

    override fun applyOptions(context: Context, builder: GlideBuilder) {
        super.applyOptions(context, builder)

        val circularProgressDrawable = CircularProgressDrawable(context)
        circularProgressDrawable.apply {
            setColorSchemeColors(R.color.primaryColor, R.color.primaryContainerColor, R.color.white)
            centerRadius = 30f
            strokeWidth = 5f
        }

        builder.setDefaultRequestOptions(
            RequestOptions()
                .placeholder(circularProgressDrawable)
                .error(R.drawable.img_not_found)
                .fallback(R.drawable.img_not_found)
        )
    }

}