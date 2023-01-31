package com.example.appsomefood

import com.example.appsomefood.AuthAndAuthorize.AuthAndRegFragment
import com.example.appsomefood.AuthFragment.AuthFragment
import com.example.appsomefood.AuthSuccessForCreator.CreatorListFragment
import com.example.appsomefood.AuthSuccessForNonCreator.ClientListFragment
import com.example.appsomefood.FavoriteFragment.FavoriteFragment
import com.example.appsomefood.Orders.OrdersFragment
import com.example.appsomefood.OrdersInWork.OrdersInWorkFragment
import com.example.appsomefood.RegistrationFragment.RegistrationFragment
import com.example.appsomefood.fragmentContainer.ContainerFragment
import com.example.appsomefood.profileFragment.ProfileFragment
import com.github.terrakok.cicerone.androidx.FragmentScreen

object Screens {
    fun routeToHomeFragment() = FragmentScreen { AuthAndRegFragment() }
    fun routeToRegistrationFragment() = FragmentScreen { RegistrationFragment() }
    fun routeToAuthFragment() = FragmentScreen { AuthFragment() }
    fun routeToListFragment() = FragmentScreen { ClientListFragment() }
    fun routeToProfileFragment() = FragmentScreen { ProfileFragment() }
    fun routeToFavoriteFragment() = FragmentScreen { FavoriteFragment() }
    fun routeToFragmentContainer() = FragmentScreen { ContainerFragment() }
    fun routeToOrdersFragment() = FragmentScreen{OrdersFragment()}
    fun routeToCreatorList() = FragmentScreen{CreatorListFragment()}
    fun routeToOrdersWork() = FragmentScreen{OrdersInWorkFragment()}

    }
