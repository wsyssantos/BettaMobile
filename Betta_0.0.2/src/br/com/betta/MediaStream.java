package br.com.betta;

import br.com.betta.R;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.media.*;
import android.media.MediaPlayer.OnBufferingUpdateListener;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.media.MediaPlayer.OnSeekCompleteListener;
import android.media.MediaPlayer.OnVideoSizeChangedListener;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Vibrator;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.VideoView;

/**
 * 
 * @author Guilherme Marques Camarão 
 * 
 */

public class MediaStream extends Activity implements OnPreparedListener, OnErrorListener, OnSeekBarChangeListener {
	
	
	/**
	 * Controles de Volume
	 */
	private Button botaoVolume;
	private int maxV;
	private int curV;
	private AudioManager am;
	
	/**
	 * Inteiro para controle de Visibilidade
	 */
	private Integer controlerHide = 0; 
	
	/**
	 * Objeto que contem o VideoView
	 */
	private VideoView vv;
	
	/**
	 * Flag indica se o Video está pronto para começar a rodar
	 * Flag muda para true se o video começou a executar 
	 */
	private boolean readyToPlay;
		
	/**
	 * Button responsável pelo botão Play de visualização
	 */
	private Button play;
	
	/**
	 * Button responsável pelo botão Stop na visualização
	 */	
	private Button stop;
		
	/**
	 * TextView contem a duração do arquivo de video
	 */
	private TextView mediaTime;
	
	/**
	 * TextView contem o tempo já assistido do video
	 */
	private TextView mediaTimeElapsed;
	
	/**
	 * CountDownTimer usado para calcular o tempo já assistido
	 */
	private CountDownTimer timer;
	
	/**
	 * ProgressBar:
	 * - FIRST indica o progresso da visualização da midia (tempo assistido)
	 * - SECOND indica o progresso do BUFFERING de midia (quando o video já está carregado no celular)
	 */
	private SeekBar progress;
	private SeekBar progressVolume;
	
	/**
	 *  LinearLayout usado para mostrar as opções caso usuário clique na tela
	 */
	private LinearLayout layoutBotoes;
	private RelativeLayout layoutVolume;
	
	/**
	 * ProgressDialog exibe uma Dialog com a mensagem "Loading..."
	 */
	private ProgressDialog loading;
	
	/**
	 * Vibrator usado quando clicamos nos botões
	 */
	private Vibrator vibrator;
	
	/** Metodo chamado quando a Activity é criada pela primeira vez*/
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		//Deixar a aplicação em pé, caso deitada usar Landscape
		this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        
		setContentView(R.layout.xmlplayer);

		/* Variaveis de inicialização */
		vv = (VideoView) findViewById(R.id.videoView1);
		// listeners para o VideoView:
		vv.setOnErrorListener(this);
		vv.setOnPreparedListener(this);

		play = (Button) findViewById(R.id.buttonPlay);
		stop = (Button) findViewById(R.id.buttonPause);
		botaoVolume = (Button) findViewById(R.id.buttonVolume);
		
		mediaTime = (TextView) findViewById(R.id.time);
		mediaTimeElapsed = (TextView) findViewById(R.id.timeElapsed);

		progress = (SeekBar) findViewById(R.id.progressBar);
		loading = new ProgressDialog(this);
		loading.setMessage("Carregando video, aguarde...");

		vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

		readyToPlay = false;
		
		//Exibe a mensagem de carregamento do video
		loading.show();

		readyToPlay = false;
		stopMedia(null);

		//URL para testes
		Uri uri = Uri.parse(getIntent().getExtras().getString("urlFilme"));
		//Pega o nome do arquivo
		//mediaInfo.setText(uri.getLastPathSegment());

		//Seta o link que o video deve começar a executar
		vv.setVideoURI(uri);
		vv.requestFocus();
		
		layoutBotoes = (LinearLayout) findViewById(R.id.linearLayoutBottom);
		layoutVolume = (RelativeLayout) findViewById(R.id.relativeLayoutVolume);
		
		progressVolume = (SeekBar) findViewById(R.id.progressVolume);
		
		am = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
		maxV = am.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
		curV = am.getStreamVolume(AudioManager.STREAM_MUSIC);
		
		progressVolume.setOnSeekBarChangeListener(this);
		progressVolume.setMax(maxV);
		progressVolume.setProgress(curV);
	}
        
    /**
     * implementação invocada quando ocorre erros durante o carregamento do video ou a visualização
     * 
     * @param player 	MediaPlayer onde os erros se referem
     * @param what		tipo de erro
     * @param extra		informação mais especifica do erro
     * 
     * @return TRUE quando um erro é lançado, FALSE quando não
     */
    public boolean onError(MediaPlayer player, int what, int extra) {
    	loading.hide();
    	return false;
    }
    
	/**
	 * implementação invocada quando o video esta pronto para executar
	 * 
	 * @param mp	MediaPlayer esta pronto
	 */
	public void onPrepared(MediaPlayer mp) {
		Log.d(this.getClass().getName(), "prepared");
		mp.setLooping(true);
		loading.hide();

		// onVideoSizeChangedListener declaration
		mp.setOnVideoSizeChangedListener(new OnVideoSizeChangedListener() {
			// Verifique o tamanho do vídeo (é um vídeo se o tamanho é definido, se áudio não)
			@Override
			public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {
				if (width != 0 && height != 0) {
					Log.d(this.getClass().getName(), "logo off");
				} else {
					Log.d(this.getClass().getName(), "logo on");
				}
			}
		});

		// onBufferingUpdateListener declaration
		mp.setOnBufferingUpdateListener(new OnBufferingUpdateListener() {
			// exibe a informação atualizada sobre o progresso do BUFFERING
			@Override
			public void onBufferingUpdate(MediaPlayer mp, int percent) {
				Log.d(this.getClass().getName(), "percent: " + percent);
				progress.setSecondaryProgress(percent);
			}
		});

		// onSeekCompletionListener declaration
		mp.setOnSeekCompleteListener(new OnSeekCompleteListener() {
			// mostram quadro atual depois de mudar a posição de reprodução
			@Override
			public void onSeekComplete(MediaPlayer mp) {
				if (!mp.isPlaying()) {
					playMedia(play);
					playMedia(null);
				}
				mediaTimeElapsed.setText(countTime(vv.getCurrentPosition()));
			}
		});

		//Caso estiver para completar não faça nada = null
		mp.setOnCompletionListener(null);

		readyToPlay = true;
		
		//Pega a duração do video
		int time = vv.getDuration();
		
		//Pega o tempo já visto do video
		int time_elapsed = vv.getCurrentPosition();
				
		//Coloca o progressBar no tempo já visto até agora
		progress.setProgress(time_elapsed);

		// atualiza o atual tempo de reprodução a cada 500ms antes de parar
		timer = new CountDownTimer(time, 500) {

			@Override
			//Cada vez que o metodo CountDownTimer tickar ou seja a cada meio segundo executa essa rotina
			public void onTick(long millisUntilFinished) {
				//Pega o tempo ja visto de video
				mediaTimeElapsed.setText(countTime(vv.getCurrentPosition()));
				float a = vv.getCurrentPosition();
				
				//Pega a duração
				float b = vv.getDuration();
				
				//Seta a barra de progresso no metodo Progress que é o primeiro que determina o tempo visto de video
				//Exemplo video de 10 minutos, onde foram vistos já 3 minutos
				//a=3
				//b=10
				//(a/b) = (3/10) = 0,3 * 100 = 30%
				progress.setProgress((int) (a / b * 100));
				
				//Controle de visibilidade
				controlerHide++;
				if(controlerHide == 20)
				{
					layoutVolume.setVisibility(RelativeLayout.GONE);
					layoutBotoes.setVisibility(LinearLayout.GONE);
					controlerHide = 0;
				}					
			}

			@Override
			public void onFinish() {
				//Se o video terminar o botão Stop deve desaparecer
				stopMedia(null);
			}
		};

		// onTouchListener declaration para o progress IMPORTANTE
		progress.setOnTouchListener(new OnTouchListener() {

			// Caso o progressBar sejá movido para avançar ou retroceder
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				//VERIFICAR SE NÃO É MAIS RAPIDO POR NO LISTENER DO SEEKBAR
				ProgressBar pb = (ProgressBar) v;

				int newPosition = (int) (100 * event.getX() / pb.getWidth());
				if (newPosition > pb.getSecondaryProgress()) {
					newPosition = pb.getSecondaryProgress();
				}

				switch (event.getAction()) {
				// update position when finger is DOWN/MOVED/UP
				case MotionEvent.ACTION_DOWN:
				case MotionEvent.ACTION_MOVE:
				case MotionEvent.ACTION_UP:
					pb.setProgress(newPosition);
					//Caso, o filme esteja em 3 minutos com duração total de 10 minutos e queira avançar para o 4 minuto, então:
					//newPosition = 40% = 40 
					//vv.getDuration = 10 minutos
					//40 * 10 = 400 / 100 = 4 minuto
					vv.seekTo((int) newPosition * vv.getDuration() / 100);
					break;
				}
				return true;
			}
		});

		// onTouchListener declaration para o VideoView MINHA IMPLEMENTACAO
		vv.setOnTouchListener(new OnTouchListener() {

			// Caso o progressBar sejá movido para avançar ou retroceder
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				
				//Exibe o Layout novamente por 3 segundos.
				if (readyToPlay) {
					layoutVolume.setVisibility(RelativeLayout.VISIBLE);
					layoutBotoes.setVisibility(LinearLayout.VISIBLE);
				}
				
				return true;
			}
		});

		
		//Atualiza o tempo visto para o agora atual
		mediaTime.setText(countTime(time));
		//Atualiza o tempo restante de video uma vez que o tempo atual mudou.
		mediaTimeElapsed.setText(countTime(time_elapsed));
		
		//Inicia o video, como se estivessem apertado Play
		playMedia(play);
	}
    
    /**
     * Converte o tempo de millisecundos para minutos e secundos, proprio para o media player
     * 
     * @param miliseconds	tempo do video em millisecundos
     * @return	tempo no formato minutos:secundos
     */
    public String countTime(int miliseconds) {
    	String timeInMinutes = new String();
    	int minutes = miliseconds / 60000;
    	int seconds = (miliseconds % 60000)/1000;
    	//se os secundos forem menores do que 10 segundos então não pode ir 3 segundos e sim 03 segundos por isso o "if" implicito
    	timeInMinutes = minutes + ":" + (seconds<10?"0" + seconds:seconds);
		return timeInMinutes;
    }
    
    /**
     * Start ou Pause a reprodução do video, quando o botão Play é apertado
     * 
     * @param v	View 'o evento de toque foi enviado para'
     */
	public void playMedia(View v) {
		buttonVibrate();
		if (readyToPlay) {
			if (v == play) {
				//Se foi apertado o botão Play então o mesmo fica como Gone = some, para que o pause possa se sobresair, e o stop fica visivel
				play.setVisibility(Button.GONE);
				stop.setVisibility(Button.VISIBLE);

				//O video começa a rolar, e o tempo também para que as atualizações a cada meio segundo entrem em vigor
				vv.start();
				timer.start();
			} else {
				//O oposto
				stop.setVisibility(Button.GONE);
				play.setVisibility(Button.VISIBLE);

				vv.pause();
				timer.cancel();
			}
		}
    }
    
    /**
     * Pause e voltar o video, quando o botão Stop é apertado
     * 
     * @param v	View 'o evento de toque foi enviado para'
     */
	public void stopMedia(View v) {
		buttonVibrate();
		if (play.getVisibility() == Button.GONE) {
			//Se o botão play estiver sumido, então ele deve aparecer e o stop sumir
			stop.setVisibility(Button.GONE);
			play.setVisibility(Button.VISIBLE);
		}

		if (vv.getCurrentPosition() != 0) {
			//Se o video estiver já começado, pause-o e mande o para o inicio
			vv.pause();
			vv.seekTo(0);
			timer.cancel();

			//Reajustar o progressBar e o tempo decorrido
			mediaTimeElapsed.setText(countTime(vv.getCurrentPosition()));
			progress.setProgress(0);
		}
	}

	@Override
	//Se algum botão for precionado
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		//Se for o botão Voltar
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			//Imprimi no logCat e finaliza a execução do video
			Log.d(this.getClass().getName(), "back button pressed");
			MediaStream.this.finish();
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	//Ciclo de vida da activity onStop()
	public void onStop() {
		vv.stopPlayback();
		if (timer != null) {
			timer.cancel();
		}
		super.onStop();
	}

	//Metodo para vibrar
	public void buttonVibrate() {
		vibrator.cancel();
		vibrator.vibrate(100); // vibrar por 100ms
	}

	
	//Liteners do AudioManager
	@Override
	public void onProgressChanged(SeekBar arg0, int progress, boolean arg2) {
		// TODO Auto-generated method stub
		am.setStreamVolume(AudioManager.STREAM_MUSIC, progress, 0);
		curV = am.getStreamVolume(AudioManager.STREAM_MUSIC);
	}

	@Override
	public void onStartTrackingTouch(SeekBar arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onStopTrackingTouch(SeekBar arg0) {
		// TODO Auto-generated method stub
		if (curV == 0)
			botaoVolume.setBackgroundResource(R.drawable.volume_off);
		else if(curV > 0 && curV <= 5)
			botaoVolume.setBackgroundResource(R.drawable.volume_1);
		else if (curV > 5 && curV <= 10)
			botaoVolume.setBackgroundResource(R.drawable.volume_2);
		else if (curV > 10)
			botaoVolume.setBackgroundResource(R.drawable.volume_3);
	}
	
	public void volumeOff(View v)
	{
		if(am.getStreamVolume(AudioManager.STREAM_MUSIC) == 0)
		{
			am.setStreamVolume(AudioManager.STREAM_MUSIC, 7, 0);
			botaoVolume.setBackgroundResource(R.drawable.volume_2);
			progressVolume.setProgress(7);
		}
		else
		{
			botaoVolume.setBackgroundResource(R.drawable.volume_off);
			am.setStreamVolume(AudioManager.STREAM_MUSIC, 0, 0);
			progressVolume.setProgress(0);
		}
	}
}