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

fun getFoods(): List<FoodDb> {
    return listOf(
        FoodDb(
            "1",
            "Vegetable Shepherd's Pie",
            "https://www.themealdb.com/images/media/meals/w8umt11583268117.jpg",
            "Add Ingredients: 12 cups chopped mixed vegetables 1 cup chopped fresh mushrooms 1 cup pearl onions TOPPING: Preheat oven to 450°.\n" +
                    "Bake potatoes on a foil-lined baking sheet until tender, about 45 minutes.\n" +
                    "Let cool slightly, then peel.\n" +
                    "Press potatoes through a ricer, food mill, or colander into a large bowl.\n" +
                    "Add butter; stir until well blended.\n" +
                    "Stir in milk.\n" +
                    "Season to taste with salt.\n" +
                    "FILLING: Soak dried porcini in 3 cups hot water; set aside.\n" +
                    "Combine lentils, 1 garlic clove, 1 tsp.\n" +
                    "salt, and 4 cups water in a medium saucepan.\n" +
                    "Bring to a boil; reduce heat and simmer, stirring occasionally, until lentils are tender but not mushy, 15–20 minutes.\n" +
                    "Drain lentils and discard garlic.\n" +
                    "Heat 3 Tbsp.\n" +
                    "oil in a large heavy pot over medium heat.\n" +
                    "Add onions and cook, stirring occasionally, until soft, about 12 minutes.\n" +
                    "Add chopped garlic and cook for 1 minute.\n" +
                    "Stir in tomato paste.\n" +
                    "Cook, stirring constantly, until tomato paste is caramelized, 2–3 minutes.\n" +
                    "Add bay leaves and wine; stir, scraping up any browned bits.\n" +
                    "Stir in porcini, slowly pouring porcini soaking liquid into pan but leaving any sediment behind.\n" +
                    "Bring to a simmer and cook until liquid is reduced by half, about 10 minutes.\n" +
                    "Stir in broth and cook, stirring occasionally, until reduced by half, about 45 minutes.\n" +
                    "Strain mixture into a large saucepan and bring to a boil; discard solids in strainer.\n" +
                    "Stir cornstarch and 2 Tbsp.\n" +
                    "water in a small bowl to dissolve.\n" +
                    "Add cornstarch mixture; simmer until thickened, about 5 minutes.\n" +
                    "Whisk in miso.\n" +
                    "Season sauce with salt and pepper.\n" +
                    "Set aside.\n" +
                    "Preheat oven to 450°.\n" +
                    "Toss vegetables and pearl onions with remaining 2 Tbsp.\n" +
                    "oil, 5 garlic cloves, and rosemary sprigs in a large bowl; season with salt and pepper.\n" +
                    "Divide between 2 rimmed baking sheets.\n" +
                    "Roast, stirring once, until tender, 20–25 minutes.\n" +
                    "Transfer garlic cloves to a small bowl; mash well with a fork and stir into sauce.\n" +
                    "Discard rosemary.\n" +
                    "DO AHEAD: Lentils, sauce, and vegetables can be made 1 day ahead.\n" +
                    "Cover separately; chill.\n" +
                    "Arrange lentils in an even layer in a 3-qt.\n" +
                    "baking dish; set dish on a foil-lined rimmed baking sheet.\n" +
                    "Toss roasted vegetables with fresh mushrooms and chopped herbs; layer on top of lentils.\n" +
                    "Pour sauce over vegetables.\n" +
                    "Spoon potato mixture evenly over.\n" +
                    "Bake until browned and bubbly, about 30 minutes.\n" +
                    "Let stand for 15 minutes before serving."
        ),
        FoodDb(
            "2",
            "Peanut Butter Cheesecake",
            "https://www.themealdb.com/images/media/meals/qtuuys1511387068.jpg",
            "Oil and line a 20cm round loose- bottomed cake tin with cling film, making it as smooth as possible.\n" +
                    "Melt the butter in a pan.\n" +
                    "Crush the biscuits by bashing them in a bag with a rolling pin, then stir them into the butter until very well coated.\n" +
                    "Press the mixture firmly into the base of the tin and chill.\n" +
                    "Soak the gelatine in water while you make the filling.\n" +
                    "Tip the ricotta into a bowl, then beat in the peanut butter and syrup.\n" +
                    "Ricotta has a slightly grainy texture so blitz until smooth with a stick blender for a smoother texture if you prefer.\n" +
                    "Take the soaked gelatine from the water and squeeze dry.\n" +
                    "Put it into a pan with the milk and heat very gently until the gelatine dissolves.\n" +
                    "Beat into the peanut mixture, then tip onto the biscuit base.\n" +
                    "Chill until set.\n" +
                    "To freeze, leave in the tin and as soon as it is solid, cover the surface with cling film, then wrap the tin with cling film and foil.\n" +
                    "To defrost, thaw in the fridge overnight.\n" +
                    "To serve, carefully remove from the tin.\n" +
                    "Whisk the cream with the sugar until it holds its shape, then spread on top of the cheesecake and scatter with the peanut brittle."
        ),
        FoodDb(
            "3",
            "Christmas cake",
            "https://www.themealdb.com/images/media/meals/ldnrm91576791881.jpg",
            "Heat oven to 160C/fan 140C/gas 3.\n" +
                    "Line the base and sides of a 20 cm round, 7.\n" +
                    "5 cm deep cake tin.\n" +
                    "Beat the butter and sugar with an electric hand mixer for 1-2 mins until very creamy and pale in colour, scraping down the sides of the bowl half way through.\n" +
                    "Stir in a spoonful of the flour, then stir in the beaten egg and the rest of the flour alternately, a quarter at a time, beating well each time with a wooden spoon.\n" +
                    "Stir in the almonds.\n" +
                    "Mix in the sherry (the mix will look curdled), then add the peel, cherries, raisins, cherries, nuts, lemon zest, spice, rosewater and vanilla.\n" +
                    "Beat together to mix, then stir in the baking powder.\n" +
                    "Spoon mixture into the tin and smooth the top, making a slight dip in the centre.\n" +
                    "Bake for 30 mins, then lower temperature to 150C/fan 130C/gas 2 and bake a further 2-2¼ hrs, until a skewer insterted in the middle comes out clean.\n" +
                    "Leave to cool in the tin, then take out of the tin and peel off the lining paper.\n" +
                    "When completely cold, wrap well in cling film and foil to store until ready to decorate.\n" +
                    "The cake will keep for several months."
        ),
        FoodDb(
            "4",
            "Rosół (Polish Chicken Soup)",
            "https://www.themealdb.com/images/media/meals/lx1kkj1593349302.jpg",
            "Add chicken to a large Dutch oven or stock pot Cover with water Bring to a boil and simmer for 2 to 2 1/2 hours, skimming any impurities off the top to insure a clear broth If your pot is big enough, add the vegetables and spices for the last hour of the cooking time My Dutch oven wasn’t big enough to hold everything, just the chicken and other bones filled the pot, so I cooked the meat/bones for the full cooking time, then removed them, and cooked the vegetables and spices separately Strain everything out of the broth Bone the chicken, pulling the meat into large chunks Slice the carrots Return the chicken and carrots to the broth Cook noodles according to package instructions if you’re using them Add noodles to bowl and then top with hot soup"
        ),
        FoodDb(
            "5",
            "Eton Mess",
            "https://www.themealdb.com/images/media/meals/uuxwvq1483907861.jpg",
            "Purée half the strawberries in a blender.\n" +
                    "Chop the remaining strawberries, reserving four for decoration.\n" +
                    "Whip the double cream until stiff peaks form, then fold in the strawberry purée and crushed meringue.\n" +
                    "Fold in the chopped strawberries and ginger cordial, if using.\n" +
                    "Spoon equal amounts of the mixture into four cold wine glasses.\n" +
                    "Serve garnished with the remaining strawberries and a sprig of mint."
        ),
        FoodDb(
            "6",
            "Japanese Katsudon",
            "https://www.themealdb.com/images/media/meals/d8f6qx1604182128.jpg",
            "STEP 1 Heat the oil in a pan, fry the sliced onion until golden brown, then add the tonkatsu (see recipe here), placing it in the middle of the pan.\n" +
                    "Mix the dashi, soy, mirin and sugar together and tip three-quarters of the mixture around the tonkatsu.\n" +
                    "Sizzle for a couple of mins so the sauce thickens a little and the tonkatsu reheats.\n" +
                    "STEP 2 Tip the beaten eggs around the tonkatsu and cook for 2-3 mins until the egg is cooked through but still a little runny.\n" +
                    "Divide the rice between two bowls, then top each with half the egg and tonkatsu mix, sprinkle over the chives and serve immediately, drizzling with a little more soy if you want an extra umami kick."
        ),
        FoodDb(
            "7",
            "Chicken Karaage",
            "https://www.themealdb.com/images/media/meals/tyywsw1505930373.jpg",
            "Add the ginger, garlic, soy sauce, sake and sugar to a bowl and whisk to combine.\n" +
                    "Add the chicken, then stir to coat evenly.\n" +
                    "Cover and refrigerate for at least 1 hour.\n" +
                    "Add 1 inch of vegetable oil to a heavy bottomed pot and heat until the oil reaches 360 degrees F.\n" +
                    "Line a wire rack with 2 sheets of paper towels and get your tongs out.\n" +
                    "Put the potato starch in a bowl Add a handful of chicken to the potato starch and toss to coat each piece evenly.\n" +
                    "Fry the karaage in batches until the exterior is a medium brown and the chicken is cooked through.\n" +
                    "Transfer the fried chicken to the paper towel lined rack.\n" +
                    "If you want the karaage to stay crispy longer, you can fry the chicken a second time, until it's a darker color after it's cooled off once.\n" +
                    "Serve with lemon wedges."
        ),
        FoodDb(
            "8",
            "Beef Bourguignon",
            "https://www.themealdb.com/images/media/meals/vtqxtu1511784197.jpg",
            "Heat a large casserole pan and add 1 tbsp goose fat.\n" +
                    "Season the beef and fry until golden brown, about 3-5 mins, then turn over and fry the other side until the meat is browned all over, adding more fat if necessary.\n" +
                    "Do this in 2-3 batches, transferring the meat to a colander set over a bowl when browned.\n" +
                    "In the same pan, fry the bacon, shallots or pearl onions, mushrooms, garlic and bouquet garni until lightly browned.\n" +
                    "Mix in the tomato purée and cook for a few mins, stirring into the mixture.\n" +
                    "This enriches the bourguignon and makes a great base for the stew.\n" +
                    "Then return the beef and any drained juices to the pan and stir through.\n" +
                    "Pour over the wine and about 100ml water so the meat bobs up from the liquid, but isn’t completely covered.\n" +
                    "Bring to the boil and use a spoon to scrape the caramelised cooking juices from the bottom of the pan – this will give the stew more flavour.\n" +
                    "Heat oven to 150C/fan 130C/gas 2.\n" +
                    "Make a cartouche: tear off a square of foil slightly larger than the casserole, arrange it in the pan so it covers the top of the stew and trim away any excess foil.\n" +
                    "Then cook for 3 hrs.\n" +
                    "If the sauce looks watery, remove the beef and veg with a slotted spoon, and set aside.\n" +
                    "Cook the sauce over a high heat for a few mins until the sauce has thickened a little, then return the beef and vegetables to the pan.\n" +
                    "To make the celeriac mash, peel the celeriac and cut into cubes.\n" +
                    "Heat the olive oil in a large frying pan.\n" +
                    "Tip in the celeriac and fry for 5 mins until it turns golden.\n" +
                    "Season well with salt and pepper.\n" +
                    "Stir in the rosemary, thyme, bay and cardamom pods, then pour over 200ml water, enough to nearly cover the celeriac.\n" +
                    "Turn the heat to low, partially cover the pan and leave to simmer for 25-30 mins.\n" +
                    "After 25-30 mins, the celeriac should be soft and most of the water will have evaporated.\n" +
                    "Drain away any remaining water, then remove the herb sprigs, bay and cardamom pods.\n" +
                    "Lightly crush with a potato masher, then finish with a glug of olive oil and season to taste.\n" +
                    "Spoon the beef bourguignon into serving bowls and place a large spoonful of the celeriac mash on top.\n" +
                    "Garnish with one of the bay leaves, if you like.\n"
        ),
        FoodDb(
            "9",
            "Laksa King Prawn Noodles",
            "https://www.themealdb.com/images/media/meals/rvypwy1503069308.jpg",
            "Heat the oil in a medium saucepan and add the chilli.\n" +
                    "Cook for 1 min, then add the curry paste, stir and cook for 1 min more.\n" +
                    "Dissolve the stock cube in a large jug in 700ml boiling water, then pour into the pan and stir to combine.\n" +
                    "Tip in the coconut milk and bring to the boil.\n" +
                    "Add the fish sauce and a little seasoning.\n" +
                    "Toss in the noodles and cook for a further 3-4 mins until softening.\n" +
                    "Squeeze in the lime juice, add the prawns and cook through until warm, about 2-3 mins.\n" +
                    "Scatter over some of the coriander.\n" +
                    "Serve in bowls with the remaining coriander and lime wedges on top for squeezing over."
        ),
        FoodDb(
            "10",
            "Chicken Parmentier",
            "https://www.themealdb.com/images/media/meals/uwvxpv1511557015.jpg",
            "For the topping, boil the potatoes in salted water until tender.\n" +
                    "Drain and push through a potato ricer, or mash thoroughly.\n" +
                    "Stir in the butter, cream and egg yolks.\n" +
                    "Season and set aside.\n" +
                    "For the filling, melt the butter in a large pan.\n" +
                    "Add the shallots, carrots and celery and gently fry until soft, then add the garlic.\n" +
                    "Pour in the wine and cook for 1 minute.\n" +
                    "Stir in the tomato purée, chopped tomatoes and stock and cook for 10–15 minutes, until thickened.\n" +
                    "Add the shredded chicken, olives and parsley.\n" +
                    "Season to taste with salt and pepper.\n" +
                    "Preheat the oven to 180C/160C Fan/Gas 4.\n" +
                    "Put the filling in a 20x30cm/8x12in ovenproof dish and top with the mashed potato.\n" +
                    "Grate over the Gruyère.\n" +
                    "Bake for 30–35 minutes, until piping hot and the potato is golden-brown."
        ),
        FoodDb(
            "11",
            "Beef and Mustard Pie",
            "https://www.themealdb.com/images/media/meals/sytuqu1511553755.jpg",
            "Preheat the oven to 150C/300F/Gas 2.\n" +
                    "Toss the beef and flour together in a bowl with some salt and black pepper.\n" +
                    "Heat a large casserole until hot, add half of the rapeseed oil and enough of the beef to just cover the bottom of the casserole.\n" +
                    "Fry until browned on each side, then remove and set aside.\n" +
                    "Repeat with the remaining oil and beef.\n" +
                    "Return the beef to the pan, add the wine and cook until the volume of liquid has reduced by half, then add the stock, onion, carrots, thyme and mustard, and season well with salt and pepper.\n" +
                    "Cover with a lid and place in the oven for two hours.\n" +
                    "Remove from the oven, check the seasoning and set aside to cool.\n" +
                    "Remove the thyme.\n" +
                    "When the beef is cool and you're ready to assemble the pie, preheat the oven to 200C/400F/Gas 6.\n" +
                    "Transfer the beef to a pie dish, brush the rim with the beaten egg yolks and lay the pastry over the top.\n" +
                    "Brush the top of the pastry with more beaten egg.\n" +
                    "Trim the pastry so there is just enough excess to crimp the edges, then place in the oven and bake for 30 minutes, or until the pastry is golden-brown and cooked through.\n" +
                    "For the green beans, bring a saucepan of salted water to the boil, add the beans and cook for 4-5 minutes, or until just tender.\n" +
                    "Drain and toss with the butter, then season with black pepper.\n" +
                    "To serve, place a large spoonful of pie onto each plate with some green beans alongside."
        ),
        FoodDb(
            "12",
            "Corned Beef and Cabbage",
            "https://www.themealdb.com/images/media/meals/xb97a81583266727.jpg",
            "1 Place corned beef in large pot or Dutch oven and cover with water.\n" +
                    "Add the spice packet that came with the corned beef.\n" +
                    "Cover pot and bring to a boil, then reduce to a simmer.\n" +
                    "Simmer approximately 50 minutes per pound or until tender.\n" +
                    "2 Add whole potatoes and peeled and cut carrots, and cook until the vegetables are almost tender.\n" +
                    "Add cabbage and cook for 15 more minutes.\n" +
                    "Remove meat and let rest 15 minutes.\n" +
                    "3 Place vegetables in a bowl and cover.\n" +
                    "Add as much broth (cooking liquid reserved in the Dutch oven or large pot) as you want.\n" +
                    "Slice meat across the grain.\n"
        ),
    )
}