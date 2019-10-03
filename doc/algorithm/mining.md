# マイニングアルゴリズム
以下のアルゴリズムが実装されています．
- アイテムセットマイニング
    - アプリオリ(幅優先)
    - バックトラック法(深さ優先)
- シーケンシャルパターンマイニング
    - PrefixPattern(深さ優先)

いずれのアルゴリズムもジェネリクスを用いており，マイニング対象を`Item`と抽象化しています．  
アルゴリズムの内部では同値性やハッシュ値を用いた実装になっているので，`Item`の`equal`メソッドと`hashCode`メソッドが正しいものであるか確認してください．  

また各アルゴリズムの内部では処理が終わるたびに，`Observer`のメソッドが呼び出されます．  
`Observer`を継承したクラスを作成し，該当するメソッドを書き換えることで，ログの出力などが可能になります．  
`Observer`はコンストラクタで与えることができます．

## アイテムセットマイニング
以下のようなインターフェースになっています．

 ```java
Set<ItemSet<Item>> execute(final Set<Set<Item>> transactions, final int theta);
```

- `transactions`:　マイニング対象のセットのセット
- `theta`: 何回以上出現するものを抽出するか

`ItemSet<Item>` は`CountablePattern`を継承しており，何度出現したか数えることができる`Set`です．


## シーケンシャルパターンマイニング
以下のようなインターフェースになっています．

 ```java
Set<SequentialPattern<Item>> execute(final Set<List<Item>> transactions, final int theta);
```

- `transactions`:　マイニング対象のリストのセット
- `theta`: 何回以上出現するものを抽出するか

`SequentialPattern<Item>` は`CountablePattern`を継承しており，何度出現したか数えることができる`List`です．

## tips
アイテムセットマイニングもシーケンシャルパターンマイニングも，`CountablePattern`を継承したインスタンスを返します．  
そのためアイテムセットマイニング，もしくはシーケンシャルパターンマイニングのどちらを用いるか隠蔽したい時は，
ジェネリクスを用いて以下のようにひとつの型として表現することができます．

```Java
<Pattern extends Collection<Item> & CountablePattern> 
```