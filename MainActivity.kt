package in.mahaboob.onlineservices

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { MaterialTheme(colorScheme = lightColorScheme(primary = androidx.compose.ui.graphics.Color(0xFF0757A6))) { AppRoot() } }
    }
}

private val services = listOf(
    "Aadhaar / ఆధార్", "PAN Card / పాన్ కార్డు", "Voter ID / ఓటర్ ఐడి",
    "Government Certificates / ప్రభుత్వ ధృవపత్రాలు", "Ration Card / రేషన్ కార్డు",
    "Farmer Registry / రైతు నమోదు", "PM-KISAN", "Land Records / భూమి రికార్డులు",
    "Mutation / మ్యుటేషన్", "Labour Card / లేబర్ కార్డు", "Pension Schemes / పెన్షన్ పథకాలు",
    "PMAY Housing / గృహ పథకం", "PMEGP / MUDRA Loans / రుణాలు",
    "GST / ITR / FSSAI", "Udyam MSME / ఉద్యమ్", "Passport / పాస్‌పోర్ట్",
    "Scholarship / స్కాలర్‌షిప్", "Job Applications / ఉద్యోగ దరఖాస్తులు",
    "Electricity Bill / విద్యుత్ బిల్లు", "Print / Xerox / Scan", "Other Service / ఇతర సేవ"
)

@Composable
fun AppRoot() {
    var page by remember { mutableStateOf("home") }
    var lang by remember { mutableStateOf("both") }
    Surface(Modifier.fillMaxSize(), color = androidx.compose.ui.graphics.Color(0xFFF3F7FC)) {
        Column(Modifier.fillMaxSize()) {
            Column(Modifier.fillMaxWidth().padding(18.dp)) {
                Text("MAHABOOB", color = MaterialTheme.colorScheme.primary, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.ExtraBold)
                Text("ONLINE SERVICES", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Text("CSC CENTER · KYATHANPALLY · 9542084119", style = MaterialTheme.typography.labelSmall)
            }
            when (page) {
                "form" -> CustomerForm(onBack = { page = "home" })
                "admin" -> AdminEntry(onBack = { page = "home" })
                else -> Home(onApply = { page = "form" }, onAdmin = { page = "admin" })
            }
        }
    }
}

@Composable
fun Home(onApply: () -> Unit, onAdmin: () -> Unit) {
    Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(horizontal = 16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Card(shape = RoundedCornerShape(18.dp), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primary)) {
            Column(Modifier.padding(18.dp)) {
                Text("మీకు కావలసిన సేవను ఎంచుకోండి", color = androidx.compose.ui.graphics.Color.White, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                Text("Choose a service and send your request", color = androidx.compose.ui.graphics.Color.White)
            }
        }
        Text("POPULAR SERVICES / ముఖ్యమైన సేవలు", fontWeight = FontWeight.Bold)
        services.chunked(2).forEach { pair ->
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                pair.forEach { service ->
                    Card(Modifier.weight(1f), onClick = onApply, shape = RoundedCornerShape(14.dp)) {
                        Text(service, Modifier.padding(14.dp), fontWeight = FontWeight.SemiBold, minLines = 2)
                    }
                }
                if (pair.size == 1) Spacer(Modifier.weight(1f))
            }
        }
        Button(onClick = onApply, modifier = Modifier.fillMaxWidth().height(54.dp)) { Text("APPLY NOW / దరఖాస్తు చేయండి") }
        OutlinedButton(onClick = onAdmin, modifier = Modifier.fillMaxWidth()) { Text("ADMIN LOGIN / నిర్వాహక లాగిన్") }
        Text("WhatsApp: 9542084119", Modifier.align(Alignment.CenterHorizontally), color = MaterialTheme.colorScheme.primary)
        Spacer(Modifier.height(18.dp))
    }
}

@Composable
fun CustomerForm(onBack: () -> Unit) {
    val context = LocalContext.current
    var name by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var village by remember { mutableStateOf("Kyathanpally") }
    var details by remember { mutableStateOf("") }
    var selected by remember { mutableStateOf(services.first()) }
    var expanded by remember { mutableStateOf(false) }
    Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
        TextButton(onClick = onBack) { Text("← Back / వెనక్కి") }
        Text("Customer Request / కస్టమర్ అభ్యర్థన", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
        OutlinedTextField(name, { name = it }, label = { Text("Name / పేరు *") }, modifier = Modifier.fillMaxWidth(), singleLine = true)
        OutlinedTextField(phone, { if (it.length <= 10 && it.all(Char::isDigit)) phone = it }, label = { Text("Mobile / మొబైల్ *") }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone), modifier = Modifier.fillMaxWidth(), singleLine = true)
        OutlinedTextField(village, { village = it }, label = { Text("Village / గ్రామం") }, modifier = Modifier.fillMaxWidth(), singleLine = true)
        ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = { expanded = !expanded }) {
            OutlinedTextField(selected, {}, readOnly = true, label = { Text("Select Service / సేవ ఎంచుకోండి *") }, trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded) }, modifier = Modifier.menuAnchor().fillMaxWidth())
            ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                services.forEach { s -> DropdownMenuItem(text = { Text(s) }, onClick = { selected = s; expanded = false }) }
            }
        }
        OutlinedTextField(details, { details = it }, label = { Text("Details / అదనపు వివరాలు") }, modifier = Modifier.fillMaxWidth(), minLines = 3)
        Text("Aadhaar number, OTP, bank PIN or passwords enter చేయవద్దు. అవసరమైన పత్రాలను కేంద్రంలో ధృవీకరించండి.", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.error)
        Button(onClick = {
            if (name.isBlank() || phone.length != 10) return@Button
            val id = "MOB" + System.currentTimeMillis().toString().takeLast(7)
            val msg = "Mahaboob Online Services - New Request\nRequest ID: $id\nName: $name\nPhone: $phone\nVillage: $village\nService: $selected\nDetails: $details"
            val encoded = URLEncoder.encode(msg, StandardCharsets.UTF_8.toString())
            val uri = Uri.parse("https://wa.me/919542084119?text=$encoded")
            context.startActivity(Intent(Intent.ACTION_VIEW, uri))
        }, modifier = Modifier.fillMaxWidth().height(52.dp)) { Text("SUBMIT & OPEN WHATSAPP / పంపండి") }
        Text("గమనిక: WhatsAppలో Send నొక్కాలి. ఈ ప్రారంభ వెర్షన్‌లో Cloudలో Request సేవ్ కాదు.", style = MaterialTheme.typography.bodySmall)
    }
}

@Composable
fun AdminEntry(onBack: () -> Unit) {
    var pin by remember { mutableStateOf("") }
    Column(Modifier.fillMaxSize().padding(18.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        TextButton(onClick = onBack) { Text("← Back / వెనక్కి") }
        Text("Admin Login / నిర్వాహక లాగిన్", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
        Text("ఈ స్క్రీన్ డిజైన్ మాత్రమే. నిజమైన సురక్షిత Admin Login, Cloud Requests చూడటానికి Firebase Authentication మరియు Database కనెక్ట్ చేయాలి.")
        OutlinedTextField(pin, { pin = it }, label = { Text("Admin PIN (not activated)") }, modifier = Modifier.fillMaxWidth(), keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword))
        Button(onClick = { }, enabled = false, modifier = Modifier.fillMaxWidth()) { Text("LOGIN — CLOUD SETUP PENDING") }
    }
}
