package com.uk.progresstracker.Fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.TextView;

import com.uk.progresstracker.Model.Report;
import com.uk.progresstracker.R;
import com.uk.progresstracker.Utils;

import java.util.Calendar;

import io.realm.Realm;
import io.realm.Sort;

/**
 * Created by usman on 14-09-2018.
 */

public class HomeFragment extends Fragment {
	
	
	private Realm realm;
	
	private TextView tvDate;
	
	private TextView tvRank;
	private TextView tvRankName;
	private TextView tvSuccess;
	private TextView tvSuccessName;
	private TextView tvCollection;
	private TextView tvCollectionName;
	private TextView tvWtLoss;
	private TextView tvWtLossName;
	private TextView tvAvgWtLoss;
	private TextView tvAvgWtLossName;
	private TextView tvPenalty;
	private TextView tvPenaltyName;
	private TextView tvActivity;
	private TextView tvActivityName;
	
	private Calendar selectedDate;
	
	
	@Nullable
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		
		return LayoutInflater.from(getContext())
			.inflate(R.layout.home_layout, container, false);
		
	}
	
	
	@Override
	public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		
		initialize(view);
		setUI();
		
	}
	
	private void initialize(View view) {
		
		realm = Realm.getDefaultInstance();
		
		selectedDate = Calendar.getInstance();
		
		//Setting time to absolute start of the day
		selectedDate.set(Calendar.HOUR_OF_DAY, 0);
		selectedDate.set(Calendar.MINUTE, 0);
		selectedDate.set(Calendar.SECOND, 0);
		selectedDate.set(Calendar.MILLISECOND, 0);
		
		int monthIndex = selectedDate.get(Calendar.MONTH);
		
		tvDate = view.findViewById(R.id.tvDate);
		tvDate.setText(Utils.formatToDate(System.currentTimeMillis()));
		
		tvRank = view.findViewById(R.id.tvRank);
		tvRankName = view.findViewById(R.id.tvRankName);
		tvSuccess = view.findViewById(R.id.tvSuccess);
		tvSuccessName = view.findViewById(R.id.tvSuccessName);
		tvCollection = view.findViewById(R.id.tvCollection);
		tvCollectionName = view.findViewById(R.id.tvCollectionName);
		tvWtLoss = view.findViewById(R.id.tvWeighLoss);
		tvWtLossName = view.findViewById(R.id.tvWeighLossName);
		tvAvgWtLoss = view.findViewById(R.id.tvAvgWeightLosss);
		tvAvgWtLossName = view.findViewById(R.id.tvAvgWeightLosssName);
		tvPenalty = view.findViewById(R.id.tvPenalty);
		tvPenaltyName = view.findViewById(R.id.tvPenaltyName);
		tvActivity = view.findViewById(R.id.tvActivity);
		tvActivityName = view.findViewById(R.id.tvActivityName);
		
		view.findViewById(R.id.ll)
			.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					showDatePicker();
				}
			});
		
		
	}
	
	private void showDatePicker() {
		
		if (!isAdded() || getContext() == null)
			return;
		
		AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
		
		View view = LayoutInflater.from(getContext())
			.inflate(R.layout.date_picker_dialog, null);
		
		builder.setView(view);
		
		final DatePicker datePicker = view.findViewById(R.id.datePicker);
		
		TextView tvCancel = view.findViewById(R.id.tvCancel);
		TextView tvOk = view.findViewById(R.id.tvOk);
		
		final AlertDialog dialog = builder.create();
		dialog.show();
		
		tvCancel.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});
		
		tvOk.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				int monthIndex = datePicker.getMonth();
				int year = datePicker.getYear();
				
				
				selectedDate.set(Calendar.MONTH, monthIndex);
				selectedDate.set(Calendar.YEAR, year);
				selectedDate.set(Calendar.DAY_OF_MONTH, datePicker.getDayOfMonth());
				
				//Setting it to absolute start of the day
				selectedDate.set(Calendar.HOUR_OF_DAY, 0);
				selectedDate.set(Calendar.MINUTE, 0);
				selectedDate.set(Calendar.SECOND, 0);
				selectedDate.set(Calendar.MILLISECOND, 0);
				
				
				tvDate.setText(Utils.formatToDate(selectedDate.getTimeInMillis()));
				
				setUI();
				
				dialog.dismiss();
				
			}
		});
		
	}
	
	private void setUI() {
		
		setHighestRank();
		setHighestSuccessPercent();
		setHighestCollection();
		setHighestWtLoss();
		setHighestAvgWtLoss();
		setHighestPenalty();
		setHighestActivity();
		
	}
	
	private void setHighestActivity() {
		
		
		Report report = realm.where(Report.class)
			.sort("activity", Sort.DESCENDING)
			.greaterThanOrEqualTo("timestamp", selectedDate.getTimeInMillis())
			.lessThan("timestamp", selectedDate.getTimeInMillis() + Utils.DAY_IN_MILLIS)
			.findFirst();
		
		if (report != null) {
			tvActivity.setText(String.valueOf(report.getActivity()));
			
			String name = Utils.getNameFromReportId(report.getId());
			tvActivityName.setText(name);
		} else {
			
			tvActivity.setText("--");
			tvActivityName.setText("--");
			
		}
		
	}
	
	private void setHighestPenalty() {
		
		Report report = realm.where(Report.class)
			.sort("penalty", Sort.DESCENDING)
			.greaterThanOrEqualTo("timestamp", selectedDate.getTimeInMillis())
			.lessThan("timestamp", selectedDate.getTimeInMillis() + Utils.DAY_IN_MILLIS)
			.findFirst();
		
		if (report != null) {
			tvPenalty.setText(String.valueOf(report.getPenalty()));
			
			String name = Utils.getNameFromReportId(report.getId());
			tvPenaltyName.setText(name);
		} else {
			
			tvPenalty.setText("--");
			tvPenaltyName.setText("--");
			
		}
		
		
	}
	
	private void setHighestRank() {
		
		
		Report report = realm.where(Report.class)
			.sort("rank", Sort.ASCENDING)
			.greaterThanOrEqualTo("timestamp", selectedDate.getTimeInMillis())
			.lessThan("timestamp", selectedDate.getTimeInMillis() + Utils.DAY_IN_MILLIS)
			.findFirst();
		
		if (report != null) {
			tvRank.setText(String.valueOf(report.getRank()));
			String name = Utils.getNameFromReportId(report.getId());
			tvRankName.setText(name);
		} else {
			
			tvRank.setText("--");
			tvRankName.setText("--");
			
		}
		
	}
	
	
	private void setHighestSuccessPercent() {
		
		Report report
			= realm.where(Report.class)
			.sort("successPercentage", Sort.DESCENDING)
			.greaterThanOrEqualTo("timestamp", selectedDate.getTimeInMillis())
			.lessThan("timestamp", selectedDate.getTimeInMillis() + Utils.DAY_IN_MILLIS)
			.findFirst();
		
		if (report != null) {
			
			String percent = report.getSuccessPercentage() + " %";
			
			tvSuccess.setText(percent);
			tvSuccessName.setText(Utils.getNameFromReportId(report.getId()));
			
		} else {
			
			tvSuccess.setText("--");
			tvSuccessName.setText("--");
			
		}
		
	}
	
	private void setHighestCollection() {
		
		Report report
			= realm.where(Report.class)
			.sort("collection", Sort.DESCENDING)
			.greaterThanOrEqualTo("timestamp", selectedDate.getTimeInMillis())
			.lessThan("timestamp", selectedDate.getTimeInMillis() + Utils.DAY_IN_MILLIS)
			.findFirst();
		
		
		if (report != null) {
			
			String collection = report.getCollection() + " Rs";
			
			tvCollection.setText(collection);
			tvCollectionName.setText(Utils.getNameFromReportId(report.getId()));
			
			
		} else {
			
			tvCollection.setText("--");
			tvCollectionName.setText("--");
			
		}
		
	}
	
	private void setHighestWtLoss() {
		
		Report report
			= realm.where(Report.class)
			.sort("weightLoss", Sort.DESCENDING)
			.greaterThanOrEqualTo("timestamp", selectedDate.getTimeInMillis())
			.lessThan("timestamp", selectedDate.getTimeInMillis() + Utils.DAY_IN_MILLIS)
			.findFirst();
		
		
		if (report != null) {
			
			String wtLoss = report.getWeightLoss() + " Kg";
			
			tvWtLoss.setText(wtLoss);
			tvWtLossName.setText(Utils.getNameFromReportId(report.getId()));
			
		} else {
			
			tvWtLoss.setText("--");
			tvWtLossName.setText("--");
			
		}
		
	}
	
	private void setHighestAvgWtLoss() {
		
		Report report
			= realm.where(Report.class)
			.sort("avgWeightLoss", Sort.DESCENDING)
			.greaterThanOrEqualTo("timestamp", selectedDate.getTimeInMillis())
			.lessThan("timestamp", selectedDate.getTimeInMillis() + Utils.DAY_IN_MILLIS)
			.findFirst();
		
		
		if (report != null) {
			
			String avgWtLoss = String.valueOf(report.getAvgWeightLoss());
			
			tvAvgWtLoss.setText(avgWtLoss);
			tvAvgWtLossName.setText(Utils.getNameFromReportId(report.getId()));
			
			
		} else {
			
			tvAvgWtLoss.setText("--");
			tvAvgWtLossName.setText("--");
			
		}
		
		
	}
	
	
}
