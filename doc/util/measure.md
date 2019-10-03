# Measure
以下のように，特定の処理の実行時間を計測することができるクラスです．  

```java
// hoge.execute()はStringを返すメソッド
final MeasuredResult<String> result = Measure.time(() -> hoge.execute());

// hoge.execute()の返り値を取得できる 
final String value = result.getValue());

 // hoge.execute()の実行時間が取得できる
final Duration duration = result.getDuration();
```
