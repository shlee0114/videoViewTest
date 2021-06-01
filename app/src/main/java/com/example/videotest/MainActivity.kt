package com.example.videotest

import android.annotation.SuppressLint
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    //raw파일 위치
    private val DEFAULT_FILE_LOCATION by lazy { "android.resource://$packageName/raw/" }
    //재생할 비디오 파일명(이름만 기입)
    private val DEFAULT_VIDEO_NAME = "sample_mp4_file"

    //반복 시간
    //단위 : msec
    private val LOOP_TIME = 1000L
    private val LOOP_IMMEDIATELY = 0L


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        init()
    }

    private fun init(){
        val testVideoURi = Uri.parse("$DEFAULT_FILE_LOCATION$DEFAULT_VIDEO_NAME")

        testVideoView.setVideoURI(testVideoURi)
        testVideoView.setOnPreparedListener { preparedPlayer -> preparedPlayer!!.start()}

        testVideoSendBackOneSec.setOnClickListener(SeekOneSecBtnClick())
        testVideoSendForwardOneSec.setOnClickListener(SeekOneSecBtnClick())

        testVideoView.postDelayed(UpdateVideoTimeText(), LOOP_TIME)
    }

    //텍스트 뷰에 테스트하고 있는 비디오 뷰의 진행 시간 / 총 시간을 입력
    inner class UpdateVideoTimeText : Runnable {
        @SuppressLint("SetTextI18n")
        override fun run() {
            testVideoDuration.text = "${testVideoView.currentPosition / 1000} / ${testVideoView.duration / 1000}"
            if(testVideoView.isPlaying){
                testVideoView.postDelayed(this, LOOP_TIME)
            }
        }
    }

    inner class SeekOneSecBtnClick : View.OnClickListener {
        val FORWARD = R.id.testVideoSendForwardOneSec
        val BACK = R.id.testVideoSendBackOneSec

        override fun onClick(v: View?) {
            v?.let {
                testVideoView.seekTo(testVideoView.currentPosition +
                        when (it.id) {
                            FORWARD -> -1000
                            BACK -> 1000
                            else -> 0
                        }
                )
                testVideoView.postDelayed(UpdateVideoTimeText(), LOOP_IMMEDIATELY)
            }
        }
    }
}