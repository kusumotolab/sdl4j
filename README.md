# sdl4j
このリポジトリは楠本研究室でJavaを用いて研究をする際によく実装する処理をまとめたライブラリになっています．

## How To Use
build.gradleに以下を追加してくだい．
```groovy
repositories {
    maven { url 'https://jitpack.io' }
}

dependencies {
    implementation 'com.github.kusumotolab:sdl4j:master'
}
```

## 機能
### アルゴリズム
- [マイニングアルゴリズム](./doc/algorithm/mining.md)

### ユーティリティ
- [実行時間の計測](./doc/util/measure.md)
- [コマンドライン](./doc/util/commandline.md)