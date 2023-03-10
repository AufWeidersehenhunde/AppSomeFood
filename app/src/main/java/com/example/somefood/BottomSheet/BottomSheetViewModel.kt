package com.example.appsomefood.BottomSheet

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.appsomefood.DBandProvider.FoodDb
import com.example.appsomefood.repository.Reference
import com.example.appsomefood.repository.RepositoryBottomSheet
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class BottomSheetViewModel (
    private val repositoryBottomSheet: RepositoryBottomSheet,
    private val preference:Reference
) : ViewModel() {
    val user = preference.getValue("pref").toString()
    private val _number = MutableStateFlow<Int>(1)
    val number: MutableStateFlow<Int> = _number
    private val _food = MutableStateFlow<FoodDb?>(null)
    val food: MutableStateFlow<FoodDb?> = _food


    fun minusSome(){
        if (_number.value > 1) {
            _number.value = _number.value - 1
        }
    }

    fun plusSome() {
        _number.value = _number.value + 1
    }

    fun takeFood(it:String) {
        viewModelScope.launch(Dispatchers.IO) {
            repositoryBottomSheet.takeFoodForSheet(it).collect{
                _food.value = it
            }
        }
    }

    fun putInOrder(idFood:String, time:String, volume:Int){
        viewModelScope.launch(Dispatchers.IO)  {
            repositoryBottomSheet.addFoodToOrder(idFood,user, time, volume)
        }
    }
}