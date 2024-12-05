package com.sisindia.ai.android.features.videocall

//import kotlinx.android.synthetic.main.activity_moninput_cards.*
import android.content.Intent
import android.os.Message
import androidx.databinding.DataBindingUtil
import com.sisindia.ai.android.R
import com.sisindia.ai.android.base.IopsBaseActivity
import com.sisindia.ai.android.databinding.ActivityVideoCallBinding
import org.jitsi.meet.sdk.JitsiMeetActivity
import org.jitsi.meet.sdk.JitsiMeetConferenceOptions
import java.net.URL

/**
 * Created by Ashu Rajput on 6/2/2024.
 */
class VideoCallActivity : IopsBaseActivity() {

//    private lateinit var viewModel: MonInputViewModel
//    private lateinit var binding: ActivityMoninputCardsBinding

    override fun getLayoutResource(): Int {
        return R.layout.activity_video_call
    }

    override fun initViewModel() {
//        viewModel = getAndroidViewModel(MonInputViewModel::class.java) as MonInputViewModel
    }

    override fun initViewBinding() {
        val binding = DataBindingUtil.setContentView(this, layoutResource) as ActivityVideoCallBinding
//        binding.vm = viewModel
        binding.executePendingBindings()
    }

    override fun onCreated() {
        openJitsiMeeting()
    }

    override fun extractBundle() {
    }

    override fun initViewState() {
        liveData.observe(this) { message: Message ->
            when (message.what) {
            }
        }
    }

    private fun openJitsiMeeting() {
        try {
//            https://8x8.vc/vpaas-magic-cookie-32fa148572974b38bf9a8e1ad44689bb/SampleAppSubstantialStomachsGuessAccurately
           /* val options = JitsiMeetConferenceOptions.Builder().setServerURL(URL("https://8x8.vc"))
                .setRoom("vpaas-magic-cookie-32fa148572974b38bf9a8e1ad44689bb/SampleAppSubstantialStomachsGuessAccurately")
                .setFeatureFlag("chat.enabled", false).setFeatureFlag("invite.enabled", false)
                .setFeatureFlag("meeting-name.enabled", false)
                .setFeatureFlag("meeting-password.enabled", false).setFeatureFlag("pip.enabled", false)
                .setFeatureFlag("raise-hand.enabled", false).setFeatureFlag("recording.enabled", false)
                .setFeatureFlag("tile-view.enabled", false).setFeatureFlag("toolbox.alwaysVisible", false)
                .setFeatureFlag("welcomepage.enabled", false).build()
            JitsiMeetActivity.launch(this, options)*/


            val options = JitsiMeetConferenceOptions.Builder().setServerURL(URL("https://meet.jit.si"))
                .setRoom("testRoom")
                .setFeatureFlag("chat.enabled", false)
                .setFeatureFlag("invite.enabled", false).setFeatureFlag("meeting-name.enabled", false)
                .setFeatureFlag("meeting-password.enabled", false).setFeatureFlag("pip.enabled", false)
                .setFeatureFlag("raise-hand.enabled", false).setFeatureFlag("recording.enabled", false)
                .setFeatureFlag("tile-view.enabled", false).setFeatureFlag("toolbox.alwaysVisible", false)
                .setFeatureFlag("welcomepage.enabled", false).build()

            JitsiMeetActivity.launch(this, options)


        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

    override fun onDestroy() {
        super.onDestroy()

    }

    override fun onResume() {
        super.onResume()
    }
}