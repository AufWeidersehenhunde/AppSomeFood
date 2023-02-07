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
import com.example.appsomefood.Orders.OrdersViewModel
import com.example.appsomefood.OrdersInWork.OrdersInWorkViewModel
import com.example.appsomefood.RegistrationFragment.RegistrationViewModel
import com.example.appsomefood.fragmentContainer.ContainerViewModel
import com.example.appsomefood.profileFragment.ProfileViewModel
import com.example.appsomefood.repository.*
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
    viewModel { ProfileViewModel(get(), get(), get(), get(), get()) }
    viewModel { FavoriteViewModel(get()) }
    viewModel { ContainerViewModel(get(), get(), get()) }
    viewModel { OrdersViewModel(get(), get()) }
    viewModel { BottomSheetViewModel(get(), get()) }
    viewModel { CreatorListViewModel(get()) }
    viewModel { DialogViewModel(get()) }
    viewModel { DialogForTakeOrderViewModel(get(), get()) }
    viewModel { OrdersInWorkViewModel(get(), get()) }
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
    single { RepositoryOrders(get()) }
    single { RepositoryProfileData(get()) }
    single { RepositoryFavorite(get(), get()) }
    single { RepositoryBottomSheet(get()) }
    single { RepositoryFood(get()) }
    single { Reference(get()) }
}

fun getFoods(): List<FoodDb> {
    return listOf(
        FoodDb(
            "1",
            "Vegetable Shepherd's Pie",
            "https://www.themealdb.com/images/media/meals/w8umt11583268117.jpg",
            ""
        ),
        FoodDb(
            "2",
            "Peanut Butter Cheesecake",
            "https://www.themealdb.com/images/media/meals/qtuuys1511387068.jpg",
            ""
        ),
        FoodDb(
            "3",
            "Christmas cake",
            "https://www.themealdb.com/images/media/meals/ldnrm91576791881.jpg",
            ""
        ),
        FoodDb(
            "4",
            "Rosół (Polish Chicken Soup)",
            "https://www.themealdb.com/images/media/meals/lx1kkj1593349302.jpg",
            ""
        ),
        FoodDb(
            "5",
            "Eton Mess",
            "https://www.themealdb.com/images/media/meals/uuxwvq1483907861.jpg",
            ""
        ),
        FoodDb(
            "6",
            "Japanese Katsudon",
            "https://www.themealdb.com/images/media/meals/d8f6qx1604182128.jpg",
            ""
        ),
        FoodDb(
            "7",
            "Chicken Karaage",
            "https://www.themealdb.com/images/media/meals/tyywsw1505930373.jpg",
            ""
        ),
        FoodDb(
            "8",
            "Beef Bourguignon",
            "https://www.themealdb.com/images/media/meals/vtqxtu1511784197.jpg",
            ""
        ),
        FoodDb(
            "9",
            "Laksa King Prawn Noodles",
            "https://www.themealdb.com/images/media/meals/rvypwy1503069308.jpg",
            ""
        ),
        FoodDb(
            "10",
            "Chicken Parmentier",
            "https://www.themealdb.com/images/media/meals/uwvxpv1511557015.jpg",
            ""
        ),
        FoodDb(
            "11",
            "Beef and Mustard Pie",
            "https://www.themealdb.com/images/media/meals/sytuqu1511553755.jpg",
            ""
        ),
        FoodDb(
            "12",
            "Corned Beef and Cabbage",
            "https://www.themealdb.com/images/media/meals/xb97a81583266727.jpg",
            ""
        ),
    )
}