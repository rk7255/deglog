package jp.ryuk.deglog.ui.newdiary

import android.util.Log
import androidx.lifecycle.*
import jp.ryuk.deglog.data.Diary
import jp.ryuk.deglog.data.DiaryDao
import jp.ryuk.deglog.data.Profile
import jp.ryuk.deglog.data.ProfileDao
import jp.ryuk.deglog.utilities.convertLongToDateStringInTime
import jp.ryuk.deglog.utilities.convertStringToFloat
import jp.ryuk.deglog.utilities.convertUnit
import jp.ryuk.deglog.utilities.deg
import kotlinx.coroutines.*
import java.util.*
import kotlin.math.absoluteValue


class NewDiaryViewModel(
    private val diaryId: Long,
    private val selectedName: String,
    private val diaryDatabase: DiaryDao,
    profileDatabase: ProfileDao
) : ViewModel() {

    private var isNew = true

    val names: LiveData<List<String>> = profileDatabase.getNamesLive()
    val diary: LiveData<Diary?> = diaryDatabase.getDiaryLive(diaryId)

    var date = Calendar.getInstance().timeInMillis
    var dateOfString = MediatorLiveData<String>()
    var name = MediatorLiveData<String>()
    var weight = MediatorLiveData<String>()
    var length = MediatorLiveData<String>()
    var memo = MediatorLiveData<String>()
    var weightUnit = MediatorLiveData<String>()
    var lengthUnit = MediatorLiveData<String>()

    private var _submit = MutableLiveData<Boolean>()
    val submit: LiveData<Boolean> get() = _submit

    private var _submitError = MutableLiveData<Boolean>()
    val submitError: LiveData<Boolean> get() = _submitError

    private var _onDateClick = MutableLiveData<Boolean>()
    val onDateCLick: LiveData<Boolean> get() = _onDateClick

    fun doneOnDateClick(time: Long) {
        date = time
        dateOfString.value = convertLongToDateStringInTime(date)
        _onDateClick.value = false
    }

    init {
        name.value = selectedName
        dateOfString.value = convertLongToDateStringInTime(date)
        weightUnit.value = "g"
        lengthUnit.value = "mm"
    }

    fun setValues() {
        isNew = false
        name.value = selectedName
        date = diary.value?.date ?: Calendar.getInstance().timeInMillis
        dateOfString.value = convertLongToDateStringInTime(date)

        diary.value?.let { diary ->
            diary.weight?.let { weight.value = convertUnit(it, weightUnit.value ?: "g", false) }
            diary.length?.let { length.value = convertUnit(it, lengthUnit.value ?: "mm", false) }
            diary.memo?.let { memo.value = it }
        }
    }

    fun cycleUnitWeight() {
        when (weightUnit.value) {
            "g" -> {
                weightUnit.value = "kg"
                weight.value = changeUnit(weight.value, "kg")
            }
            "kg" -> {
                weightUnit.value = "g"
                weight.value = changeUnit(weight.value, "g")

            }
        }
    }

    fun cycleUnitLength() {
        when (lengthUnit.value) {
            "mm" -> {
                lengthUnit.value = "m"
                length.value = changeUnit(length.value, "m")
            }
            "m" -> {
                lengthUnit.value = "mm"
                length.value = changeUnit(length.value, "mm")
            }
        }
    }

    private fun changeUnit(number: String?, unit: String): String {
        if (number.isNullOrBlank()) return ""
        return if (unit == "g" || unit == "mm") {
            (number.toFloat() * 1000).toInt().toString()
        } else {
            (number.toFloat() / 1000).toString()
        }
    }

    fun onSubmit() {
        if (isValid()) {
            val diary = Diary(
                date = date,
                name = name.value!!,
                weight = weight.value?.toFloat(),
                length = length.value?.toFloat(),
                memo = memo.value
            )
            if (isNew) {
                insertDiary(diary)
            } else {
                diary.id = diaryId
                updateDiary(diary)
            }
            _submit.value = true
        } else {
            _submitError.value = true
        }
    }

    private fun isValid(): Boolean = !name.value.isNullOrEmpty()

    fun onCancel() {
        _submit.value = true
    }

    fun onDate() {
        _onDateClick.value = true
    }

    private suspend fun insert(diary: Diary) {
        withContext(Dispatchers.IO) { diaryDatabase.insert(diary) }
    }

    private fun insertDiary(diary: Diary) {
        viewModelScope.launch { insert(diary) }
    }

    private suspend fun update(diary: Diary) {
        withContext(Dispatchers.IO) { diaryDatabase.update(diary) }
    }

    private fun updateDiary(diary: Diary) {
        viewModelScope.launch { update(diary) }
    }
}