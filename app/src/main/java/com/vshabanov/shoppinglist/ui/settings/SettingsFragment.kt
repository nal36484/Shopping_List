package com.vshabanov.shoppinglist.ui.settings

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.app.Person
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.vshabanov.shoppinglist.R
import com.vshabanov.shoppinglist.activity.MainActivity
import com.vshabanov.shoppinglist.databinding.FragmentSettingsBinding
import com.vshabanov.shoppinglist.ui.notifications.NotificationsFragment
import java.util.*

class SettingsFragment : Fragment() {

    private val notificationId = 101
    private val channelId = "channelID"
    val vibrate = listOf(1000L,2000L,3000L).toLongArray()

    private lateinit var settingsViewModel: SettingsViewModel
    private var _binding: FragmentSettingsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        settingsViewModel =
            ViewModelProvider(this).get(SettingsViewModel::class.java)

        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }

    @SuppressLint("UnspecifiedImmutableFlag")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val button: Button = view.findViewById(R.id.test)
        button.setOnClickListener {
            val sender = Person.Builder()
                .setName("Сообщение")
                //.setIcon(...) // можно добавить значок
                .build()

            val messagingStyle = NotificationCompat.MessagingStyle(sender)
                .addMessage("Вам пришёл новый список", Date().time, sender)

            val notificationIntent = Intent(context, MainActivity::class.java)
            notificationIntent.putExtra("NotificationsFragment", "NotificationsFragment")
            notificationIntent.flags = (Intent.FLAG_ACTIVITY_CLEAR_TOP
                    or Intent.FLAG_ACTIVITY_SINGLE_TOP)
            val intent = PendingIntent.getActivity(context, 0 , notificationIntent,
                PendingIntent.FLAG_CANCEL_CURRENT)

            val builder = context?.let { it1 ->
                NotificationCompat.Builder(it1, "Cat channel")
                    .setSmallIcon(R.drawable.ic_action_accept)
                    .setStyle(messagingStyle)
                    .setContentIntent(intent)
                    .setVibrate(vibrate)
                    .addAction(R.drawable.ic_action_accept, "Запустить", intent)
                    .setAutoCancel(true)
            }

            val channel = NotificationChannel("Notification channel", "channel", NotificationManager
                .IMPORTANCE_DEFAULT).apply {
                description = "new message"
            }

            with(context?.let { it1 -> NotificationManagerCompat.from(it1) }) {
                this?.createNotificationChannel(channel)
                if (builder != null) {
                    this?.notify(notificationId, builder.build())
                }
            }
        }
    }
}