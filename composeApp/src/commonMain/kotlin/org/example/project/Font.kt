package org.example.project

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import org.jetbrains.compose.resources.Font
import yomu.composeapp.generated.resources.JetBrainsMono_Bold
import yomu.composeapp.generated.resources.JetBrainsMono_Medium
import yomu.composeapp.generated.resources.JetBrainsMono_Regular
import yomu.composeapp.generated.resources.JetBrainsMono_SemiBold
import yomu.composeapp.generated.resources.Res
import androidx.compose.material3.Typography
import yomu.composeapp.generated.resources.JetBrainsMono_Thin


@Composable
fun jetBrainsMonoTypography(): Typography {
    val jetBrainsMonoFont = FontFamily(
        Font(Res.font.JetBrainsMono_Thin, FontWeight.Thin),



        Font(Res.font.JetBrainsMono_Regular, FontWeight.Normal),
        Font(Res.font.JetBrainsMono_Bold, FontWeight.Bold),
        Font(Res.font.JetBrainsMono_Medium, FontWeight.Medium),
        Font(Res.font.JetBrainsMono_SemiBold, FontWeight.SemiBold)
    )

    return with(MaterialTheme.typography) {
        copy(

            displayLarge = displayLarge.copy(fontFamily = jetBrainsMonoFont, fontWeight = FontWeight.Thin),
            displayMedium = displayMedium.copy(fontFamily = jetBrainsMonoFont, fontWeight = FontWeight.Thin),
            displaySmall = displaySmall.copy(fontFamily = jetBrainsMonoFont, fontWeight = FontWeight.Thin),

            headlineLarge = headlineLarge.copy(fontFamily = jetBrainsMonoFont, fontWeight = FontWeight.Thin),
            headlineMedium = headlineMedium.copy(fontFamily = jetBrainsMonoFont, fontWeight = FontWeight.Thin),
            headlineSmall = headlineSmall.copy(fontFamily = jetBrainsMonoFont, fontWeight = FontWeight.Thin),

            titleLarge = titleLarge.copy(fontFamily = jetBrainsMonoFont, fontWeight = FontWeight.Medium),
            titleMedium = titleMedium.copy(fontFamily = jetBrainsMonoFont, fontWeight = FontWeight.Medium),
            titleSmall = titleSmall.copy(fontFamily = jetBrainsMonoFont, fontWeight = FontWeight.Medium),

            labelLarge = labelLarge.copy(fontFamily = jetBrainsMonoFont, fontWeight = FontWeight.Normal),
            labelMedium = labelMedium.copy(fontFamily = jetBrainsMonoFont, fontWeight = FontWeight.Normal),
            labelSmall = labelSmall.copy(fontFamily = jetBrainsMonoFont, fontWeight = FontWeight.Normal),

            bodyLarge = bodyLarge.copy(fontFamily = jetBrainsMonoFont, fontWeight = FontWeight.Normal),
            bodyMedium = bodyMedium.copy(fontFamily = jetBrainsMonoFont, fontWeight = FontWeight.Normal),
            bodySmall = bodySmall.copy(fontFamily = jetBrainsMonoFont, fontWeight = FontWeight.Normal),
        )
    }
}
