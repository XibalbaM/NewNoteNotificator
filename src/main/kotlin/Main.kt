import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import fr.xibalba.pronoteKt.Ent
import fr.xibalba.pronoteKt.Period
import fr.xibalba.pronoteKt.PronoteKt
import fr.xibalba.pronoteKt.SessionType
import fr.xibalba.pronoteKt.getNotes
import fr.xibalba.pronoteKt.gson
import java.io.File
import java.net.URI
import java.net.URLEncoder

val gson = Gson()

suspend fun main() {
    val pronote = PronoteKt("https://0740006e.index-education.net/pronote", SessionType.STUDENT, Ent.AUVERGNE_RHONE_ALPES)
    pronote.login(System.getenv("PRONOTE_USER"), System.getenv("PRONOTE_PASS"))
    val notes = pronote.getNotes(Period.THIRD_TRIMESTER).notes
    val notesWithIds = notes.associateWith { it.dateString + it.subject + it.mark }
    val oldNotes: List<String> = URI("https://raw.githubusercontent.com/XibalbaM/NewNoteNotificator/master/notes.json")
        .toURL().readText()
        .let {
            class Token : TypeToken<List<String>>()
            gson.fromJson(it, Token().type)
        }
    val newNotes = notesWithIds.filter { it.value !in oldNotes }
    newNotes.forEach {
        val notificationText = "Nouvelle note en ${it.key.subject} : ${it.key.mark}" +
                if (it.key.title.isNotEmpty()) " (${it.key.title})" else ""
        URI("https://trigger.macrodroid.com/77230727-b9a3-4a6f-bf5a-84ddffeb1830/new_grade?text="
                + URLEncoder.encode(notificationText, "UTF-8")).toURL().readText()
    }
    val file = File("notes.json")
    file.writeText(gson.toJson(notesWithIds.values.toList()))
}