package uz.shoxrux.app.data.repository

import android.content.Intent
import android.content.pm.PackageManager
import uz.shoxrux.app.domain.model.AppModelUi
import uz.shoxrux.app.domain.repository.AppListRepository
import javax.inject.Inject

class AppListRepositoryImpl @Inject constructor(
    private val packageManager: PackageManager
) : AppListRepository {

    override suspend fun getAppList(): List<AppModelUi> {
        val launchIntents = listOf(
            Intent(Intent.ACTION_MAIN, null).apply {
                addCategory(Intent.CATEGORY_LAUNCHER)
            },
            Intent(Intent.ACTION_MAIN, null).apply {
                addCategory(Intent.CATEGORY_LEANBACK_LAUNCHER)
            }
        )

        val activities = launchIntents.flatMap { intent ->
            packageManager.queryIntentActivities(intent, 0)
        }.distinctBy { it.activityInfo.packageName }

        return activities.map { info ->
            AppModelUi(
                icon = info.loadIcon(packageManager),
                name = info.loadLabel(packageManager).toString(),
                packageName = info.activityInfo.packageName
            )
        }
    }

}
