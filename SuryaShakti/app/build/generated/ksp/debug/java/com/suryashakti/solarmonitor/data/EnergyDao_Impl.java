package com.suryashakti.solarmonitor.data;

import android.database.Cursor;
import android.os.CancellationSignal;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import java.lang.Class;
import java.lang.Exception;
import java.lang.Float;
import java.lang.Integer;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import javax.annotation.processing.Generated;
import kotlin.Unit;
import kotlin.coroutines.Continuation;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class EnergyDao_Impl implements EnergyDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<EnergyLog> __insertionAdapterOfEnergyLog;

  private final EntityDeletionOrUpdateAdapter<EnergyLog> __deletionAdapterOfEnergyLog;

  private final EntityDeletionOrUpdateAdapter<EnergyLog> __updateAdapterOfEnergyLog;

  private final SharedSQLiteStatement __preparedStmtOfDeleteOlderThan;

  public EnergyDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfEnergyLog = new EntityInsertionAdapter<EnergyLog>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `energy_logs` (`id`,`date`,`generatedKwh`,`consumedKwh`,`batteryLevel`,`weatherCondition`,`perUnitRate`,`timestamp`) VALUES (nullif(?, 0),?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final EnergyLog entity) {
        statement.bindLong(1, entity.getId());
        statement.bindString(2, entity.getDate());
        statement.bindDouble(3, entity.getGeneratedKwh());
        statement.bindDouble(4, entity.getConsumedKwh());
        statement.bindDouble(5, entity.getBatteryLevel());
        statement.bindString(6, entity.getWeatherCondition());
        statement.bindDouble(7, entity.getPerUnitRate());
        statement.bindLong(8, entity.getTimestamp());
      }
    };
    this.__deletionAdapterOfEnergyLog = new EntityDeletionOrUpdateAdapter<EnergyLog>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "DELETE FROM `energy_logs` WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final EnergyLog entity) {
        statement.bindLong(1, entity.getId());
      }
    };
    this.__updateAdapterOfEnergyLog = new EntityDeletionOrUpdateAdapter<EnergyLog>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `energy_logs` SET `id` = ?,`date` = ?,`generatedKwh` = ?,`consumedKwh` = ?,`batteryLevel` = ?,`weatherCondition` = ?,`perUnitRate` = ?,`timestamp` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final EnergyLog entity) {
        statement.bindLong(1, entity.getId());
        statement.bindString(2, entity.getDate());
        statement.bindDouble(3, entity.getGeneratedKwh());
        statement.bindDouble(4, entity.getConsumedKwh());
        statement.bindDouble(5, entity.getBatteryLevel());
        statement.bindString(6, entity.getWeatherCondition());
        statement.bindDouble(7, entity.getPerUnitRate());
        statement.bindLong(8, entity.getTimestamp());
        statement.bindLong(9, entity.getId());
      }
    };
    this.__preparedStmtOfDeleteOlderThan = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM energy_logs WHERE date < ?";
        return _query;
      }
    };
  }

  @Override
  public Object insertLog(final EnergyLog log, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfEnergyLog.insert(log);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteLog(final EnergyLog log, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __deletionAdapterOfEnergyLog.handle(log);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object updateLog(final EnergyLog log, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __updateAdapterOfEnergyLog.handle(log);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteOlderThan(final String cutoffDate,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteOlderThan.acquire();
        int _argIndex = 1;
        _stmt.bindString(_argIndex, cutoffDate);
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfDeleteOlderThan.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public LiveData<EnergyLog> getLogByDate(final String date) {
    final String _sql = "SELECT * FROM energy_logs WHERE date = ? LIMIT 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindString(_argIndex, date);
    return __db.getInvalidationTracker().createLiveData(new String[] {"energy_logs"}, false, new Callable<EnergyLog>() {
      @Override
      @Nullable
      public EnergyLog call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfDate = CursorUtil.getColumnIndexOrThrow(_cursor, "date");
          final int _cursorIndexOfGeneratedKwh = CursorUtil.getColumnIndexOrThrow(_cursor, "generatedKwh");
          final int _cursorIndexOfConsumedKwh = CursorUtil.getColumnIndexOrThrow(_cursor, "consumedKwh");
          final int _cursorIndexOfBatteryLevel = CursorUtil.getColumnIndexOrThrow(_cursor, "batteryLevel");
          final int _cursorIndexOfWeatherCondition = CursorUtil.getColumnIndexOrThrow(_cursor, "weatherCondition");
          final int _cursorIndexOfPerUnitRate = CursorUtil.getColumnIndexOrThrow(_cursor, "perUnitRate");
          final int _cursorIndexOfTimestamp = CursorUtil.getColumnIndexOrThrow(_cursor, "timestamp");
          final EnergyLog _result;
          if (_cursor.moveToFirst()) {
            final int _tmpId;
            _tmpId = _cursor.getInt(_cursorIndexOfId);
            final String _tmpDate;
            _tmpDate = _cursor.getString(_cursorIndexOfDate);
            final float _tmpGeneratedKwh;
            _tmpGeneratedKwh = _cursor.getFloat(_cursorIndexOfGeneratedKwh);
            final float _tmpConsumedKwh;
            _tmpConsumedKwh = _cursor.getFloat(_cursorIndexOfConsumedKwh);
            final float _tmpBatteryLevel;
            _tmpBatteryLevel = _cursor.getFloat(_cursorIndexOfBatteryLevel);
            final String _tmpWeatherCondition;
            _tmpWeatherCondition = _cursor.getString(_cursorIndexOfWeatherCondition);
            final float _tmpPerUnitRate;
            _tmpPerUnitRate = _cursor.getFloat(_cursorIndexOfPerUnitRate);
            final long _tmpTimestamp;
            _tmpTimestamp = _cursor.getLong(_cursorIndexOfTimestamp);
            _result = new EnergyLog(_tmpId,_tmpDate,_tmpGeneratedKwh,_tmpConsumedKwh,_tmpBatteryLevel,_tmpWeatherCondition,_tmpPerUnitRate,_tmpTimestamp);
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Object getLogByDateSync(final String date,
      final Continuation<? super EnergyLog> $completion) {
    final String _sql = "SELECT * FROM energy_logs WHERE date = ? LIMIT 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindString(_argIndex, date);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<EnergyLog>() {
      @Override
      @Nullable
      public EnergyLog call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfDate = CursorUtil.getColumnIndexOrThrow(_cursor, "date");
          final int _cursorIndexOfGeneratedKwh = CursorUtil.getColumnIndexOrThrow(_cursor, "generatedKwh");
          final int _cursorIndexOfConsumedKwh = CursorUtil.getColumnIndexOrThrow(_cursor, "consumedKwh");
          final int _cursorIndexOfBatteryLevel = CursorUtil.getColumnIndexOrThrow(_cursor, "batteryLevel");
          final int _cursorIndexOfWeatherCondition = CursorUtil.getColumnIndexOrThrow(_cursor, "weatherCondition");
          final int _cursorIndexOfPerUnitRate = CursorUtil.getColumnIndexOrThrow(_cursor, "perUnitRate");
          final int _cursorIndexOfTimestamp = CursorUtil.getColumnIndexOrThrow(_cursor, "timestamp");
          final EnergyLog _result;
          if (_cursor.moveToFirst()) {
            final int _tmpId;
            _tmpId = _cursor.getInt(_cursorIndexOfId);
            final String _tmpDate;
            _tmpDate = _cursor.getString(_cursorIndexOfDate);
            final float _tmpGeneratedKwh;
            _tmpGeneratedKwh = _cursor.getFloat(_cursorIndexOfGeneratedKwh);
            final float _tmpConsumedKwh;
            _tmpConsumedKwh = _cursor.getFloat(_cursorIndexOfConsumedKwh);
            final float _tmpBatteryLevel;
            _tmpBatteryLevel = _cursor.getFloat(_cursorIndexOfBatteryLevel);
            final String _tmpWeatherCondition;
            _tmpWeatherCondition = _cursor.getString(_cursorIndexOfWeatherCondition);
            final float _tmpPerUnitRate;
            _tmpPerUnitRate = _cursor.getFloat(_cursorIndexOfPerUnitRate);
            final long _tmpTimestamp;
            _tmpTimestamp = _cursor.getLong(_cursorIndexOfTimestamp);
            _result = new EnergyLog(_tmpId,_tmpDate,_tmpGeneratedKwh,_tmpConsumedKwh,_tmpBatteryLevel,_tmpWeatherCondition,_tmpPerUnitRate,_tmpTimestamp);
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public LiveData<List<EnergyLog>> getLast30DaysLogs() {
    final String _sql = "SELECT * FROM energy_logs ORDER BY date DESC LIMIT 30";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return __db.getInvalidationTracker().createLiveData(new String[] {"energy_logs"}, false, new Callable<List<EnergyLog>>() {
      @Override
      @Nullable
      public List<EnergyLog> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfDate = CursorUtil.getColumnIndexOrThrow(_cursor, "date");
          final int _cursorIndexOfGeneratedKwh = CursorUtil.getColumnIndexOrThrow(_cursor, "generatedKwh");
          final int _cursorIndexOfConsumedKwh = CursorUtil.getColumnIndexOrThrow(_cursor, "consumedKwh");
          final int _cursorIndexOfBatteryLevel = CursorUtil.getColumnIndexOrThrow(_cursor, "batteryLevel");
          final int _cursorIndexOfWeatherCondition = CursorUtil.getColumnIndexOrThrow(_cursor, "weatherCondition");
          final int _cursorIndexOfPerUnitRate = CursorUtil.getColumnIndexOrThrow(_cursor, "perUnitRate");
          final int _cursorIndexOfTimestamp = CursorUtil.getColumnIndexOrThrow(_cursor, "timestamp");
          final List<EnergyLog> _result = new ArrayList<EnergyLog>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final EnergyLog _item;
            final int _tmpId;
            _tmpId = _cursor.getInt(_cursorIndexOfId);
            final String _tmpDate;
            _tmpDate = _cursor.getString(_cursorIndexOfDate);
            final float _tmpGeneratedKwh;
            _tmpGeneratedKwh = _cursor.getFloat(_cursorIndexOfGeneratedKwh);
            final float _tmpConsumedKwh;
            _tmpConsumedKwh = _cursor.getFloat(_cursorIndexOfConsumedKwh);
            final float _tmpBatteryLevel;
            _tmpBatteryLevel = _cursor.getFloat(_cursorIndexOfBatteryLevel);
            final String _tmpWeatherCondition;
            _tmpWeatherCondition = _cursor.getString(_cursorIndexOfWeatherCondition);
            final float _tmpPerUnitRate;
            _tmpPerUnitRate = _cursor.getFloat(_cursorIndexOfPerUnitRate);
            final long _tmpTimestamp;
            _tmpTimestamp = _cursor.getLong(_cursorIndexOfTimestamp);
            _item = new EnergyLog(_tmpId,_tmpDate,_tmpGeneratedKwh,_tmpConsumedKwh,_tmpBatteryLevel,_tmpWeatherCondition,_tmpPerUnitRate,_tmpTimestamp);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public LiveData<List<EnergyLog>> getAllLogs() {
    final String _sql = "SELECT * FROM energy_logs ORDER BY date DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return __db.getInvalidationTracker().createLiveData(new String[] {"energy_logs"}, false, new Callable<List<EnergyLog>>() {
      @Override
      @Nullable
      public List<EnergyLog> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfDate = CursorUtil.getColumnIndexOrThrow(_cursor, "date");
          final int _cursorIndexOfGeneratedKwh = CursorUtil.getColumnIndexOrThrow(_cursor, "generatedKwh");
          final int _cursorIndexOfConsumedKwh = CursorUtil.getColumnIndexOrThrow(_cursor, "consumedKwh");
          final int _cursorIndexOfBatteryLevel = CursorUtil.getColumnIndexOrThrow(_cursor, "batteryLevel");
          final int _cursorIndexOfWeatherCondition = CursorUtil.getColumnIndexOrThrow(_cursor, "weatherCondition");
          final int _cursorIndexOfPerUnitRate = CursorUtil.getColumnIndexOrThrow(_cursor, "perUnitRate");
          final int _cursorIndexOfTimestamp = CursorUtil.getColumnIndexOrThrow(_cursor, "timestamp");
          final List<EnergyLog> _result = new ArrayList<EnergyLog>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final EnergyLog _item;
            final int _tmpId;
            _tmpId = _cursor.getInt(_cursorIndexOfId);
            final String _tmpDate;
            _tmpDate = _cursor.getString(_cursorIndexOfDate);
            final float _tmpGeneratedKwh;
            _tmpGeneratedKwh = _cursor.getFloat(_cursorIndexOfGeneratedKwh);
            final float _tmpConsumedKwh;
            _tmpConsumedKwh = _cursor.getFloat(_cursorIndexOfConsumedKwh);
            final float _tmpBatteryLevel;
            _tmpBatteryLevel = _cursor.getFloat(_cursorIndexOfBatteryLevel);
            final String _tmpWeatherCondition;
            _tmpWeatherCondition = _cursor.getString(_cursorIndexOfWeatherCondition);
            final float _tmpPerUnitRate;
            _tmpPerUnitRate = _cursor.getFloat(_cursorIndexOfPerUnitRate);
            final long _tmpTimestamp;
            _tmpTimestamp = _cursor.getLong(_cursorIndexOfTimestamp);
            _item = new EnergyLog(_tmpId,_tmpDate,_tmpGeneratedKwh,_tmpConsumedKwh,_tmpBatteryLevel,_tmpWeatherCondition,_tmpPerUnitRate,_tmpTimestamp);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Object getTotalGeneratedBetween(final String startDate, final String endDate,
      final Continuation<? super Float> $completion) {
    final String _sql = "SELECT SUM(generatedKwh) FROM energy_logs WHERE date BETWEEN ? AND ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 2);
    int _argIndex = 1;
    _statement.bindString(_argIndex, startDate);
    _argIndex = 2;
    _statement.bindString(_argIndex, endDate);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<Float>() {
      @Override
      @Nullable
      public Float call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final Float _result;
          if (_cursor.moveToFirst()) {
            final Float _tmp;
            if (_cursor.isNull(0)) {
              _tmp = null;
            } else {
              _tmp = _cursor.getFloat(0);
            }
            _result = _tmp;
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Object getTotalConsumedBetween(final String startDate, final String endDate,
      final Continuation<? super Float> $completion) {
    final String _sql = "SELECT SUM(consumedKwh) FROM energy_logs WHERE date BETWEEN ? AND ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 2);
    int _argIndex = 1;
    _statement.bindString(_argIndex, startDate);
    _argIndex = 2;
    _statement.bindString(_argIndex, endDate);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<Float>() {
      @Override
      @Nullable
      public Float call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final Float _result;
          if (_cursor.moveToFirst()) {
            final Float _tmp;
            if (_cursor.isNull(0)) {
              _tmp = null;
            } else {
              _tmp = _cursor.getFloat(0);
            }
            _result = _tmp;
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Object getTotalLogCount(final Continuation<? super Integer> $completion) {
    final String _sql = "SELECT COUNT(*) FROM energy_logs";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<Integer>() {
      @Override
      @NonNull
      public Integer call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final Integer _result;
          if (_cursor.moveToFirst()) {
            final int _tmp;
            _tmp = _cursor.getInt(0);
            _result = _tmp;
          } else {
            _result = 0;
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
