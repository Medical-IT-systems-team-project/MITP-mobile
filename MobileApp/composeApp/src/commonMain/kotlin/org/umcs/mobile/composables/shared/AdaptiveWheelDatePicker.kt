package org.umcs.mobile.composables.shared

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import com.slapps.cupertino.adaptive.AdaptiveWidget
import com.slapps.cupertino.adaptive.ExperimentalAdaptiveApi
import com.slapps.cupertino.theme.CupertinoTheme
import dev.darkokoa.datetimewheelpicker.WheelDatePicker
import dev.darkokoa.datetimewheelpicker.core.WheelPickerDefaults
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

@OptIn(ExperimentalMaterial3Api::class, ExperimentalAdaptiveApi::class)
@Composable
fun AdaptiveWheelDatePicker(
    sheetState: SheetState,
    dismiss: (LocalDate) -> Unit,
    passedStartDate: LocalDate? = null,
) {
    val currentDate = passedStartDate ?: Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
    var currentlyPickedDate by remember { mutableStateOf(currentDate) }

    AdaptiveWidget(
        material = {
            MaterialWheelDatePicker(
                sheetState = sheetState,
                dismiss = dismiss,
                currentlyPickedDateTime = currentlyPickedDate,
                startDateTime = currentDate,
                changeCurrentlyPickedDateTime = { snappedDateTime ->
                    currentlyPickedDate = snappedDateTime
                }
            )
        },
        cupertino = {
            CupertinoWheelDatePicker(
                sheetState = sheetState,
                dismiss = dismiss,
                currentlyPickedDate = currentlyPickedDate,
                startDate = currentDate,
                changeCurrentlyPickedDate = { snappedDateTime ->
                    currentlyPickedDate = snappedDateTime
                }
            )
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CupertinoWheelDatePicker(
    sheetState: SheetState,
    dismiss: (LocalDate) -> Unit,
    currentlyPickedDate: LocalDate,
    startDate: LocalDate,
    changeCurrentlyPickedDate: (LocalDate) -> Unit,
) {
    ModalBottomSheet(
        dragHandle = { BottomSheetDefaults.HiddenShape },
        sheetState = sheetState,
        onDismissRequest = { dismiss(currentlyPickedDate) }
    ) {
        Box(
            modifier = Modifier.fillMaxWidth().height(300.dp),
            contentAlignment = Alignment.Center
        ) {
            WheelDatePicker(
                startDate = startDate,
                minDate = LocalDate(
                    year = 2020,
                    monthNumber = 1,
                    dayOfMonth = 1,
                ),
                maxDate = LocalDate(
                    year = 2030,
                    monthNumber = 12,
                    dayOfMonth = 31,
                ),
                size = DpSize(400.dp, 250.dp),
                rowCount = 3,
                textStyle = CupertinoTheme.typography.body,
                textColor = MaterialTheme.colorScheme.primary,
                selectorProperties = WheelPickerDefaults.selectorProperties(
                    enabled = false,
                )
            ) { snappedDateTime ->
                changeCurrentlyPickedDate(snappedDateTime)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MaterialWheelDatePicker(
    sheetState: SheetState,
    dismiss: (LocalDate) -> Unit,
    currentlyPickedDateTime: LocalDate,
    startDateTime: LocalDate,
    changeCurrentlyPickedDateTime: (LocalDate) -> Unit,
) {
    ModalBottomSheet(
        dragHandle = { BottomSheetDefaults.HiddenShape },
        sheetState = sheetState,
        onDismissRequest = { dismiss(currentlyPickedDateTime) }
    ) {
        Box(
            modifier = Modifier.fillMaxWidth().height(300.dp),
            contentAlignment = Alignment.Center
        ) {
            WheelDatePicker(
                startDate = startDateTime,
                minDate = LocalDate(
                    year = 2020,
                    monthNumber = 1,
                    dayOfMonth = 1,
                ),
                maxDate = LocalDate(
                    year = 2030,
                    monthNumber = 12,
                    dayOfMonth = 31,
                ),
                size = DpSize(400.dp, 250.dp),
                rowCount = 3,
                textStyle = MaterialTheme.typography.titleSmall,
                textColor = MaterialTheme.colorScheme.primary,
                selectorProperties = WheelPickerDefaults.selectorProperties(
                    enabled = true,
                    shape = RoundedCornerShape(10.dp),
                    color = Color(0xFFf1faee).copy(alpha = 0.2f),
                    border = BorderStroke(2.dp, Color(0xFFf1faee))
                )
            ) { snappedDateTime ->
                changeCurrentlyPickedDateTime(snappedDateTime)
            }
        }
    }
}