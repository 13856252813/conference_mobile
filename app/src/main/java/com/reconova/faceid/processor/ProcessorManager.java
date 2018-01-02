package com.reconova.faceid.processor;

import android.os.AsyncTask;

import java.util.concurrent.Executor;

public class ProcessorManager {

	public boolean isWorking;

	public ProcessorManager() {
		isWorking = false;
	}
	
	public boolean isWorking() {
		return isWorking;
	}

	/**
	 * 开始处理事件
	 * @param iProcessor
	 */
	@SuppressWarnings("unchecked")
	public void startProcessor(IProcessor iProcessor) {
		if (!isWorking) {
			isWorking = true;
			ProcessorTask task = new ProcessorTask(iProcessor);
			task.execute();
		}
	}

	@SuppressWarnings("unchecked")
	public void startProcessor(IProcessor iProcessor, Executor excutor) {
		if (!isWorking) {
			isWorking = true;
			ProcessorTask task = new ProcessorTask(iProcessor);
			task.executeOnExecutor(excutor);
		}
	}
	
	public static interface IProcessor {
		/**
		 * 事件处理
		 * 
		 * @return 处理的结果
		 */
		public Object onProcess();

		/**
		 * 完成事件处理，更新UI主界面
		 * 
		 */
		public void onPostExcute(Object result);
	}

	@SuppressWarnings("rawtypes")
	class ProcessorTask extends AsyncTask {
		private IProcessor mProcessor;

		public ProcessorTask(IProcessor iProcessor) {
			mProcessor = iProcessor;
		}

		/**
		 * 子线程中处理事件
		 */
		protected Object doInBackground(Object... arg0) {
			return mProcessor.onProcess();
		}

		/**
		 * 完成事件处理，更新UI界面
		 */
		protected void onPostExecute(Object result) {
			mProcessor.onPostExcute(result);
			isWorking = false;
		}
	}
}
