package org.umcs.mobile.composables.case_list_view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import com.slapps.cupertino.adaptive.AdaptiveTopAppBar
import com.slapps.cupertino.adaptive.AdaptiveWidget
import mobileapp.composeapp.generated.resources.Res
import mobileapp.composeapp.generated.resources.caduceus
import mobileapp.composeapp.generated.resources.caretrack
import mobileapp.composeapp.generated.resources.cross_logo
import org.jetbrains.compose.resources.painterResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyAdaptiveTopBar(
    scrollBehavior: TopAppBarScrollBehavior,
    isDoctor: Boolean,
    navigationIcon: @Composable () -> Unit,
) {
   AdaptiveWidget(
       material = {
           CenterAlignedTopAppBar(
               colors = TopAppBarDefaults.topAppBarColors(
                   containerColor = MaterialTheme.colorScheme.background,
                   scrolledContainerColor = MaterialTheme.colorScheme.background
               ),
               scrollBehavior = scrollBehavior,
               title = {
                   Row(
                       verticalAlignment = Alignment.CenterVertically,
                       horizontalArrangement = Arrangement.spacedBy((-35).dp)
                   ) {
                       if (isDoctor) {
                           Icon(
                               painter = painterResource(Res.drawable.caduceus),
                               contentDescription = null,
                               modifier = Modifier.fillMaxHeight(0.8f)
                           )
                       } else {
                           Icon(
                               painter = painterResource(Res.drawable.cross_logo),
                               contentDescription = null,
                               modifier = Modifier.fillMaxHeight(0.8f)
                           )
                       }
                       Icon(
                           painter = painterResource(Res.drawable.caretrack),
                           contentDescription = null
                       )
                   }
               }
           )
       },
       cupertino = {

       }
   )
}

