package app.spidy.orbexample.hilt

import android.content.Context
import app.spidy.orbexample.Repo
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {
    @Singleton
    @Provides
    fun provideRepo(@ApplicationContext context: Context): Repo {
        return Repo(context)
    }
}