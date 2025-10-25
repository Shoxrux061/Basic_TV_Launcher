package uz.shoxrux.app.data.di

import android.content.Context
import android.content.pm.PackageManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import uz.shoxrux.app.data.repository.AppListRepositoryImpl
import uz.shoxrux.app.domain.repository.AppListRepository
import uz.shoxrux.app.domain.use_case.GetAppListUseCase
import uz.shoxrux.app.presentation.screens.main.MainScreenReducer
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun providePackageManager(
        @ApplicationContext context: Context
    ): PackageManager = context.packageManager

    @Provides
    @Singleton
    fun providePackageName(
        @ApplicationContext context: Context
    ): String = context.packageName

    @Provides
    @Singleton
    fun provideAppRepository(
        packageManager: PackageManager
    ): AppListRepository {
        return AppListRepositoryImpl(packageManager)
    }

    @Provides
    @Singleton
    fun provideAppUseCase(
        repository: AppListRepository
    ): GetAppListUseCase {
        return GetAppListUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideMainScreenReducer(): MainScreenReducer = MainScreenReducer()


}