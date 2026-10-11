package com.example.ui.screens

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.PortfolioRepository
import com.example.ui.theme.CyanPrimary
import com.example.ui.theme.EmeraldAccent

@Composable
fun ContactScreen(
    isWideScreen: Boolean = false,
    modifier: Modifier = Modifier
) {
    val profile = PortfolioRepository.candidateProfile
    val context = LocalContext.current

    var senderName by remember { mutableStateOf("") }
    var senderEmail by remember { mutableStateOf("") }
    var messageText by remember { mutableStateOf("") }
    var inquiryRole by remember { mutableStateOf("Full-Time Data Analyst") }

    fun copyToClipboard(text: String, label: String) {
        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText(label, text)
        clipboard.setPrimaryClip(clip)
        Toast.makeText(context, "$label copied to clipboard!", Toast.LENGTH_SHORT).show()
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = if (isWideScreen) 24.dp else 16.dp),
        contentPadding = PaddingValues(top = 16.dp, bottom = 100.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Column {
                Text(
                    text = "Get in Touch & Connect",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = "Inquire about data analytics roles, projects, or technical consulting",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        if (isWideScreen) {
            // Desktop Side-by-Side Layout
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(20.dp)
                ) {
                    // Left Column: Direct channels and share resume
                    Column(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        DirectChannelsCard(profile = profile, context = context, onCopy = ::copyToClipboard)
                        ShareResumeButton(profile = profile, context = context)
                    }

                    // Right Column: Direct message form
                    Column(
                        modifier = Modifier.weight(1f)
                    ) {
                        InquiryMessageForm(
                            senderName = senderName,
                            onNameChange = { senderName = it },
                            senderEmail = senderEmail,
                            onEmailChange = { senderEmail = it },
                            messageText = messageText,
                            onMessageChange = { messageText = it },
                            inquiryRole = inquiryRole,
                            profile = profile,
                            context = context
                        )
                    }
                }
            }
        } else {
            // Handheld Mobile Stacked Layout
            item {
                DirectChannelsCard(profile = profile, context = context, onCopy = ::copyToClipboard)
            }

            item {
                ShareResumeButton(profile = profile, context = context)
            }

            item {
                InquiryMessageForm(
                    senderName = senderName,
                    onNameChange = { senderName = it },
                    senderEmail = senderEmail,
                    onEmailChange = { senderEmail = it },
                    messageText = messageText,
                    onMessageChange = { messageText = it },
                    inquiryRole = inquiryRole,
                    profile = profile,
                    context = context
                )
            }
        }
    }
}

@Composable
private fun DirectChannelsCard(
    profile: Map<String, String>,
    context: Context,
    onCopy: (String, String) -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .border(1.dp, CyanPrimary.copy(alpha = 0.4f), RoundedCornerShape(16.dp)),
        color = MaterialTheme.colorScheme.surface.copy(alpha = 0.90f)
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            Text(
                text = "DIRECT CHANNELS",
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.Bold,
                color = CyanPrimary,
                letterSpacing = 1.sp
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Email Channel
            ContactChannelRow(
                icon = Icons.Default.Email,
                label = "Email Address",
                value = profile["email"] ?: "aneshsahu03@gmail.com",
                actionLabel = "Compose Email",
                onAction = {
                    val intent = Intent(Intent.ACTION_SENDTO).apply {
                        data = Uri.parse("mailto:${profile["email"]}")
                        putExtra(Intent.EXTRA_SUBJECT, "Career / Project Inquiry: Thrusha Sahu Data Analytics")
                    }
                    context.startActivity(Intent.createChooser(intent, "Send Email"))
                },
                onCopy = { onCopy(profile["email"] ?: "", "Email") }
            )

            Spacer(modifier = Modifier.height(12.dp))
            HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f))
            Spacer(modifier = Modifier.height(12.dp))

            // GitHub Channel
            ContactChannelRow(
                icon = Icons.Default.Code,
                label = "GitHub Repository & Portfolio",
                value = "github.com/thrushasahu-12",
                actionLabel = "Open GitHub",
                onAction = {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(profile["github"]))
                    context.startActivity(intent)
                },
                onCopy = { onCopy(profile["github"] ?: "", "GitHub Link") }
            )

            Spacer(modifier = Modifier.height(12.dp))
            HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f))
            Spacer(modifier = Modifier.height(12.dp))

            // Web Portfolio Channel
            ContactChannelRow(
                icon = Icons.Default.Language,
                label = "Live Web Portfolio",
                value = "thrushasahu-12.github.io/Resume-Website-portfolio",
                actionLabel = "Open Web",
                onAction = {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(profile["portfolioUrl"]))
                    context.startActivity(intent)
                },
                onCopy = { onCopy(profile["portfolioUrl"] ?: "", "Portfolio Link") }
            )
        }
    }
}

@Composable
private fun ShareResumeButton(
    profile: Map<String, String>,
    context: Context
) {
    Button(
        onClick = {
            val formattedResume = buildString {
                appendLine("==========================================")
                appendLine("THRUSHA SAHU - DATA ANALYST & BI SPECIALIST")
                appendLine("Email: ${profile["email"]}")
                appendLine("GitHub: ${profile["github"]}")
                appendLine("Web Portfolio: ${profile["portfolioUrl"]}")
                appendLine("==========================================")
                appendLine("\nSUMMARY:")
                appendLine(profile["summary"])
                appendLine("\nKEY ACHIEVEMENTS (SERVICENOW / MAGNIT GLOBAL):")
                PortfolioRepository.experiences.first().keyAchievements.forEach {
                    appendLine("• $it")
                }
                appendLine("\nCORE COMPETENCIES:")
                PortfolioRepository.skillGroups.forEach { group ->
                    appendLine("${group.category}: ${group.skills.joinToString { it.name }}")
                }
            }

            val shareIntent = Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                putExtra(Intent.EXTRA_SUBJECT, "Thrusha Sahu - Resume (Data Analyst)")
                putExtra(Intent.EXTRA_TEXT, formattedResume)
            }
            context.startActivity(Intent.createChooser(shareIntent, "Share Complete Resume"))
        },
        modifier = Modifier
            .fillMaxWidth()
            .testTag("share_resume_btn"),
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = EmeraldAccent,
            contentColor = Color(0xFF0A0F1D)
        )
    ) {
        Icon(Icons.Default.Share, contentDescription = null, modifier = Modifier.size(18.dp))
        Spacer(modifier = Modifier.width(8.dp))
        Text("Share Full Formatted Resume", fontWeight = FontWeight.Bold)
    }
}

@Composable
private fun InquiryMessageForm(
    senderName: String,
    onNameChange: (String) -> Unit,
    senderEmail: String,
    onEmailChange: (String) -> Unit,
    messageText: String,
    onMessageChange: (String) -> Unit,
    inquiryRole: String,
    profile: Map<String, String>,
    context: Context
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .border(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.35f), RoundedCornerShape(16.dp)),
        color = MaterialTheme.colorScheme.surface.copy(alpha = 0.90f)
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            Text(
                text = "SEND INQUIRY OR INTERVIEW REQUEST",
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.Bold,
                color = CyanPrimary,
                letterSpacing = 0.8.sp
            )

            Spacer(modifier = Modifier.height(14.dp))

            OutlinedTextField(
                value = senderName,
                onValueChange = onNameChange,
                label = { Text("Your Name / Company") },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("sender_name_input"),
                shape = RoundedCornerShape(10.dp),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(10.dp))

            OutlinedTextField(
                value = senderEmail,
                onValueChange = onEmailChange,
                label = { Text("Your Email") },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("sender_email_input"),
                shape = RoundedCornerShape(10.dp),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(10.dp))

            OutlinedTextField(
                value = messageText,
                onValueChange = onMessageChange,
                label = { Text("Message / Role Details") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(110.dp)
                    .testTag("message_text_input"),
                shape = RoundedCornerShape(10.dp)
            )

            Spacer(modifier = Modifier.height(14.dp))

            Button(
                onClick = {
                    val emailSubject = "Inquiry from $senderName for Thrusha Sahu"
                    val emailBody = "Sender: $senderName ($senderEmail)\nInquiry Type: $inquiryRole\n\nMessage:\n$messageText"
                    val intent = Intent(Intent.ACTION_SENDTO).apply {
                        data = Uri.parse("mailto:${profile["email"]}")
                        putExtra(Intent.EXTRA_SUBJECT, emailSubject)
                        putExtra(Intent.EXTRA_TEXT, emailBody)
                    }
                    context.startActivity(Intent.createChooser(intent, "Send Email Inquiry"))
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("send_inquiry_button"),
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = CyanPrimary,
                    contentColor = Color(0xFF0A0F1D)
                )
            ) {
                Icon(Icons.Default.Send, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text("Draft & Send Message", fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
private fun ContactChannelRow(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    value: String,
    actionLabel: String,
    onAction: () -> Unit,
    onCopy: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            modifier = Modifier.weight(1f),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                color = CyanPrimary.copy(alpha = 0.15f),
                shape = CircleShape
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = CyanPrimary,
                    modifier = Modifier
                        .padding(8.dp)
                        .size(18.dp)
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(
                    text = label,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = value,
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }

        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = onCopy) {
                Icon(
                    imageVector = Icons.Default.ContentCopy,
                    contentDescription = "Copy",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(18.dp)
                )
            }

            IconButton(onClick = onAction) {
                Icon(
                    imageVector = Icons.Default.OpenInNew,
                    contentDescription = actionLabel,
                    tint = CyanPrimary,
                    modifier = Modifier.size(18.dp)
                )
            }
        }
    }
}
