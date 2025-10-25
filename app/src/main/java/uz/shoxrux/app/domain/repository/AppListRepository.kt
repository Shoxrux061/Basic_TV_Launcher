package uz.shoxrux.app.domain.repository

import uz.shoxrux.app.domain.model.AppModelUi

interface AppListRepository {

    suspend fun getAppList(): List<AppModelUi>

}