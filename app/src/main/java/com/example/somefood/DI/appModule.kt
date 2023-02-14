package com.example.appsomefood

import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.appsomefood.AuthAndAuthorize.AuthAndRegViewModel
import com.example.appsomefood.AuthFragment.AuthViewModel
import com.example.appsomefood.AuthSuccessForCreator.CreatorListViewModel
import com.example.appsomefood.AuthSuccessForNonCreator.ClientListViewModel
import com.example.appsomefood.BottomSheet.BottomSheetViewModel
import com.example.appsomefood.DBandProvider.DBprovider
import com.example.appsomefood.DBandProvider.FoodDb
import com.example.appsomefood.Dao.DaoFood
import com.example.appsomefood.Dialog.DialogViewModel
import com.example.appsomefood.DialogForTakeOrder.DialogForTakeOrderViewModel
import com.example.appsomefood.FavoriteFragment.FavoriteViewModel
import com.example.appsomefood.FeedbackDialog.FeedbackDialogViewModel
import com.example.appsomefood.MainActivity.MainActivityViewModel
import com.example.appsomefood.Orders.OrdersClientViewModel
import com.example.appsomefood.RegistrationFragment.RegistrationViewModel
import com.example.appsomefood.fragmentContainer.ContainerViewModel
import com.example.appsomefood.profileFragment.ProfileViewModel
import com.example.appsomefood.repository.*
import com.example.somefood.DI.getFoods
import com.github.terrakok.cicerone.Cicerone
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import java.util.concurrent.Executors


val appModule = module {
    val cicerone = Cicerone.create()
    single { cicerone.router }
    single { cicerone.getNavigatorHolder() }

    viewModel { MainActivityViewModel(get(), get()) }
    viewModel { RegistrationViewModel(get(), get(), get()) }
    viewModel { AuthAndRegViewModel(get()) }
    viewModel { AuthViewModel(get(), get(), get(), get()) }
    viewModel { ClientListViewModel(get(), get()) }
    viewModel { ProfileViewModel(get(), get(), get(), get()) }
    viewModel { FavoriteViewModel(get()) }
    viewModel { ContainerViewModel(get(), get(), get()) }
    viewModel { OrdersClientViewModel(get(), get()) }
    viewModel { BottomSheetViewModel(get(), get()) }
    viewModel { CreatorListViewModel(get()) }
    viewModel { DialogViewModel(get()) }
    viewModel { DialogForTakeOrderViewModel(get(), get(), get()) }
    viewModel { com.example.appsomefood.OrdersInWork.OrdersCreatorViewModel(get(), get()) }
    viewModel { FeedbackDialogViewModel(get(), get()) }

    single {
        Room.databaseBuilder(
            androidApplication().applicationContext,
            DBprovider::class.java,
            "database"
        )
            .addCallback(object : RoomDatabase.Callback() {
                override fun onCreate(db: SupportSQLiteDatabase) {
                    super.onCreate(db)
                    //pre-populate data
                    Executors.newSingleThreadExecutor().execute {
                        get<DaoFood>().insertFoods(getFoods())
                    }
                }
            }).build()


    }

    single { get<DBprovider>().DaoFood() }
    single { get<DBprovider>().DaoUser() }
    single { get<DBprovider>().DaoOrders() }
    single { get<DBprovider>().DaoFavorite() }
    single { get<DBprovider>().DaoProfileInfo() }
    single { get<DBprovider>().DaoBottomSheet() }
    single { RepositoryUser(get(), get(), get()) }
    single { RepositoryOrders(get(), get()) }
    single { RepositoryProfileData(get()) }
    single { RepositoryFavorite(get(), get()) }
    single { RepositoryBottomSheet(get()) }
    single { RepositoryFood(get(), get()) }
    single { Reference(get()) }
}

