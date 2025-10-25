package uz.shoxrux.app.domain.use_case

import uz.shoxrux.app.domain.model.AppModelUi
import uz.shoxrux.app.domain.repository.AppListRepository
import javax.inject.Inject

class GetAppListUseCase @Inject constructor(
    private val repository: AppListRepository
) {

    suspend operator fun invoke(): List<AppModelUi> {
        return repository.getAppList()
    }

}