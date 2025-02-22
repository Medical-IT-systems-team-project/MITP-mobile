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
import com.slapps.cupertino.theme.CupertinoTheme
import dev.darkokoa.datetimewheelpicker.WheelDateTimePicker
import dev.darkokoa.datetimewheelpicker.core.WheelPickerDefaults
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdaptiveWheelDateTimePicker(
    sheetState: SheetState,
    dismiss: (LocalDateTime) -> Unit,
    passedStartDateTime: LocalDateTime? = null,
    endDateTime : LocalDateTime? = null
) {
    val currentDateTime = if (passedStartDateTime != null) passedStartDateTime else Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
    var currentlyPickedDateTime by remember { mutableStateOf(currentDateTime) }

    AdaptiveWidget(
        material = {
            MaterialWheelDateTimePicker(
                sheetState = sheetState,
                dismiss = dismiss,
                currentlyPickedDateTime = currentlyPickedDateTime,
                startDateTime = currentDateTime,
                changeCurrentlyPickedDateTime = { snappedDateTime ->
                    currentlyPickedDateTime = snappedDateTime
                },
                endDateTime = endDateTime
            )
        },
        cupertino = {
            CupertinoWheelDateTimePicker(
                sheetState = sheetState,
                dismiss = dismiss,
                currentlyPickedDateTime = currentlyPickedDateTime,
                startDateTime = currentDateTime,
                changeCurrentlyPickedDateTime = { snappedDateTime ->
                    currentlyPickedDateTime = snappedDateTime
                },
                        endDateTime = endDateTime

            )
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CupertinoWheelDateTimePicker(
    sheetState: SheetState,
    dismiss: (LocalDateTime) -> Unit,
    currentlyPickedDateTime: LocalDateTime,
    startDateTime: LocalDateTime,
    changeCurrentlyPickedDateTime: (LocalDateTime) -> Unit,
    endDateTime : LocalDateTime? = null
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
            WheelDateTimePicker(
                startDateTime = startDateTime,
                minDateTime = startDateTime,
                maxDateTime = endDateTime ?: LocalDateTime(
                    year = 2030,
                    monthNumber = 12,
                    dayOfMonth = 31,
                    hour = 23,
                    minute = 59
                ),
                size = DpSize(375.dp, 240.dp),
                rowCount = 5,
                textStyle = CupertinoTheme.typography.body,
                textColor = MaterialTheme.colorScheme.primary,
                selectorProperties = WheelPickerDefaults.selectorProperties(
                    enabled = false,
                )
            ) { snappedDateTime ->
                val formattedDateTime = LocalDateTime(
                    year = snappedDateTime.year,
                    monthNumber = snappedDateTime.monthNumber,
                    dayOfMonth = snappedDateTime.dayOfMonth,
                    hour = snappedDateTime.hour,
                    minute = snappedDateTime.minute,
                    second = snappedDateTime.second
                )
                changeCurrentlyPickedDateTime(formattedDateTime)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MaterialWheelDateTimePicker(
    sheetState: SheetState,
    dismiss: (LocalDateTime) -> Unit,
    currentlyPickedDateTime: LocalDateTime,
    startDateTime: LocalDateTime,
    changeCurrentlyPickedDateTime: (LocalDateTime) -> Unit,
    endDateTime : LocalDateTime? = null
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
            WheelDateTimePicker(
                startDateTime = startDateTime,
                minDateTime = startDateTime,
                maxDateTime = endDateTime ?: LocalDateTime(
                    year = 2030,
                    monthNumber = 12,
                    dayOfMonth = 31,
                    hour = 23,
                    minute = 59
                ),
                size = DpSize(400.dp, 250.dp),
                rowCount = 5,
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
