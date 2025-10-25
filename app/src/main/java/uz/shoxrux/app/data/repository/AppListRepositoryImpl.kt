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
        val mainIntent = Intent(Intent.ACTION_MAIN, null).apply {
            addCategory(Intent.CATEGORY_LAUNCHER)
        }

        val activities = packageManager.queryIntentActivities(mainIntent, 0)

        return activities.map { info ->
            AppModelUi(
                icon = info.loadIcon(packageManager),
                name = info.loadLabel(packageManager).toString(),
                intent = packageManager.getLaunchIntentForPackage(
                    info.activityInfo.packageName
                )!!
            )
        }
    }
}
