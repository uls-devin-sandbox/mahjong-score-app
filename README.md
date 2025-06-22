# 麻雀得点集計アプリ

会社の麻雀サークル向けの得点集計Webアプリケーションです。

## 技術スタック

### フロントエンド
- Vue 3
- Element Plus UI
- TypeScript
- Vite

### バックエンド
- Spring Boot 3.4
- MyBatis
- PostgreSQL
- Java 17

## 機能

### 部屋管理
- 月ごとの部屋作成
- 参加プレイヤーの登録
- ルール設定（ウマ・オカ・トビ）

### プレイヤー管理
- プレイヤーの登録・編集
- プレイヤー一覧表示

### 対局記録
- 4人対局の記録
- 得点・順位・起家・トビの記録
- 自動得点計算

### 集計機能
- 日次集計（部屋単位）
- 年間成績（プレイヤー単位）
- 統計情報の表示

## セットアップ

### 前提条件
- Docker & Docker Compose
- Node.js 18+
- Java 17+
- Maven 3.6+

### データベース起動
```bash
docker-compose up -d
```

### バックエンド起動
```bash
cd backend
mvn spring-boot:run
```

### フロントエンド起動
```bash
cd frontend
npm install
npm run dev
```

## アクセス
- フロントエンド: http://localhost:5173
- バックエンドAPI: http://localhost:8080/api

## Azureデプロイメント

このアプリケーションはAzure Static Web Appsにデプロイできます。

### 前提条件
- Azure Static Web Appsリソースの作成
- GitHubリポジトリとの連携

### 設定
- Java 17を使用
- フロントエンドビルド: `npm run build`
- バックエンドAPI: Spring Boot (Java 17)
- 設定ファイル: `staticwebapp.config.json`

### GitHub Secrets の設定
以下のシークレットをGitHubリポジトリに設定する必要があります：
- `AZURE_STATIC_WEB_APPS_API_TOKEN_AMBITIOUS_MEADOW_0BFC70C00`: Azure Static Web AppsのAPIトークン

### デプロイ
mainブランチにプッシュすると、GitHub Actionsが自動的にビルドとデプロイを実行します。

### トラブルシューティング
- **deployment_token エラー**: GitHub SecretsにAzure Static Web Apps APIトークンが正しく設定されているか確認してください
- **Java バージョンエラー**: Java 17を使用するように設定されているか確認してください

## 画面構成
- トップページ（部屋一覧）
- 部屋詳細ページ
- 対局入力ページ
- プレイヤー一覧ページ
- プレイヤー詳細ページ
- 年間成績ページ
