package com.example.workmanager_tutorial

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.work.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //Java
        //OneTimeWorkRequest workRequest = OneTimeWorkRequest.Builder(ExampleWorker::class.java)

        //Kotlin
        val workRequest = OneTimeWorkRequestBuilder<ExampleWorker>().build()
        val workManager = WorkManager.getInstance(this)
        workManager.enqueue(workRequest)

        //1회성
        //val workRequest = OneTimeWorkRequest.from(ExampleWorker::class.java)

        //반복
        //val workRequest = OneTimeWorkRequestBuilder<ExampleWorker>(15, TimeUnit.MINUTES).build()

        /*제약 조건
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)//네트워크 연결상태
            .setRequiresCharging(true)//충전상태
            .build()
        val workRequest2 = OneTimeWorkRequestBuilder<ExampleWorker>()
            .setConstraints(constraints)
            .build()*/

        //작업 연결(3 이후 4 실행)
        //val workRequest3 = OneTimeWorkRequestBuilder<ExampleWorker>().build()
        //val workRequest4 = OneTimeWorkRequestBuilder<ExampleWorker>().build()
        //workManager.apply { beginWith(workRequest3).then(workRequest4).enqueue() }

        //데이터 전달
        //val input = mapOf("a" to 100)
        /*val workRequest5 = OneTimeWorkRequestBuilder<ExampleWorker>()
            .setInputData(inputData)
            .build()*/

        //작업 취소
        //workManager.cancelWorkById(workRequest.id)

        /*유일한 작업.
        작업에 유일한 이름을 부여해 큐에 넣거나 조회, 취소 가능.
        KEEP: 작업1이 실행 대기중이거나 실행중이면 작업2는 큐에 추가되지 않음.
        REPLACE: 작업1을 취소하고 작업2를 큐에 추가.
        APPEND: 작업2를 BLOCKED(대기)로 바꾸고, 작업1이 완료되면 작업2를 큐에 추가.
        val config = workManager.beginUniqueWork("string", ExistingWorkPolicy.KEEP, workRequest)*/
    }
}

class ExampleWorker: Worker() {
    override fun doWork(): Result {
        //처리해야할 작업
        return if(isStopped) {
            Log.d("Log","it is stopped")
            Result.failure()
        } else {
            Result.success()
        }
    }
}

/*WorkManager API
앱이 종료되거나 기기가 다시 시작되어도 실행 예정인 지연 비동기 작업을 쉽게 예약할 수 있게 해줌.
안드로이드의 백그라운드 작업을 처리하는 방법 중 하나로 Android Jetpack 아키텍처 구성 요소 중 하나.
하나의 코드로 각 API 레벨 마다 비슷한 동작을 보장.
API 14 이상에서 지원.
네트워크 가용성, 충전상태 같은 작업의 제약 조건을 설정 가능.
일회성 혹은 주기적인 비동기 작업 예약할 수 있음.
모니터링, 관리.
작업을 여러개로 묶고 연결할 수 있음.
앱이 다시 시작되어도 작업 실행을 보장함.
잠자기 모드, 절전 기능 지원.
앱이 종료되어도 지연 가능하고 안정적으로 실행되어야 하는 작업을 위해 설계됨.
(예를 들어 백엔드 서비스에 로그 또는 분석을 전송, 주기적으로 애플리케이션 데이트를 서버와 동기화)
 */

/*주 사용 예시
    -지연가능한 작업의 보장된 실행: WorkManager
    서버에 로그 업로드.
    업로드, 다운로드 할 콘텐츠 암호화, 복호화.

    -외부 이벤트에 대한 응답으로 시작된 작업: FCM + WorkManager
    이메일 같은 새로운 온라인 컨텐츠 동기화.
    FCM을 사용해 앱에 알리고 WorkManager로 작업 요청을 생성해 콘텐츠 동기화.

    (모든 작업을 WorkManager를 사용하는 것을 올바른 방법이 아님.
    사용자가 현재 보고있는 UI를 빠르게 변경하는 작업, 결제 진행 등 즉시 처리해야하는
    작업은 ForgroundService나 ThreadPool, Rx 등을 사용)
 */

/*Worker
추상 클래스이며, 처리해야 하는 백그라운드 작업의 처리 코드를 상속받아 doWork()를 오버라이드.
    -doWork()
    작업을 완료하고 결과에 따라 Worker 클래스 내에 정의된 enum인 Result를 리턴.
    (SUCCESS, FAILURE, RETRY)
 */

/*WorkRequest
WorkManager의 큐에 등록해 요청하게 될 개별 작업 단위.
처리해야할 작업인 Work와 작업 반복 여부, 작업 실행 조건, 제약 사항 등에 대한 정보가 있음.
반복 여부에 따라 ontimeWorkRequest, PeriodicWorkRequest로 나뉨.
    -onTimeWorkRequest: 반복하지 않을 작업, 즉 한번만 실행할 작업의 요청을 나타냄.
    -PeriodicWorkRequest: 여러번 실행할 작업의 요청을 나타냄.
 */

/*WorkState
WorkRequest의 id, 현재 상태를 담음.
상태정보를 이용해 자신이 요청한 작업의 현재 상태 파악 가능.
ENQUEUE, RUNNING, SUCCEEDED, FAILED, BLOCKED, CANCELLED 상태를 가짐.
 */

/*WorkManager
처리해야 하는 작업(WorkRequest)을 큐에 넣어 관리.
싱글톤으로 구현되어 있음.
 */