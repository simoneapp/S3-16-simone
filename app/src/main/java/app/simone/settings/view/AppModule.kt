package app.simone.settings.view

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import dagger.Component
import dagger.Module
import dagger.Provides

/**
 * Created by Giacomo on 12/08/2017.
 */
@Module
class AppModule {
    @Provides fun provideSharedPreferences(app: Application): SharedPreferences? {
        return app.getSharedPreferences("Settings", Context.MODE_PRIVATE)
    }
}
