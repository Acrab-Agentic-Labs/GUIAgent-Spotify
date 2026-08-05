# MySpotify

一个基于 Android Jetpack Compose 的 Spotify 克隆应用。

## 项目结构

```
app/src/main/java/com/example/myspotify/
├── model/              # 数据模型
│   ├── Song.kt
│   ├── Artist.kt
│   ├── Album.kt
│   ├── Playlist.kt
│   ├── UserData.kt
│   └── PlaybackState.kt
├── data/               # 数据管理
│   ├── DataManager.kt
│   └── AssetMapper.kt  # ID 到 assets 图片路径映射
├── ui/                 # UI 页面
│   ├── common/
│   │   ├── AssetImage.kt  # 可复用的 assets 图片加载组件
│   │   ├── PlaylistMenuView.kt  # 歌单菜单页面（点击歌单三点按钮弹出）
│   │   ├── MusicMenuView.kt    # 歌曲菜单页面（点击歌曲三点按钮弹出）
│   │   └── SelectPlaylistView.kt # 选择歌单页面（从菜单 Add to playlist 进入）
│   ├── home/
│   │   ├── HomeTabView.kt         # 首页主视图（含 All/Music 内容切换）
│   │   ├── UserTabView.kt         # 用户个人页面（点击头像进入）
│   │   ├── PodcastsTabView.kt     # Podcasts 标签页内容
│   │   ├── AudiobooksTabView.kt   # Audiobooks 标签页内容
│   │   ├── PlayingTabView.kt      # 全屏播放页面（点击底部播放栏进入）
│   │   ├── MenuTabView.kt         # 播放菜单页面（点击三点按钮进入）
│   │   ├── PodcastDetailView.kt   # 播客详情页（视频预览+进度条+评论区）
│   │   ├── AudiobookDetailView.kt # 有声书详情页（封面+播放控制+书籍介绍）
│   │   ├── LyricsTabView.kt       # 全屏歌词页面（点击歌词卡片进入）
│   │   ├── CreditsTabView.kt      # 歌曲制作人员名单页面
│   │   ├── SleepTimerView.kt     # 睡眠定时器选择页面
│   │   └── AboutTheArtistTabView.kt # 艺术家介绍页面
│   ├── search/
│   │   ├── SearchTabView.kt                # 搜索主页面（三板块 + 导航）
│   │   ├── SearchForSomethingTabView.kt     # 搜索输入页（最近搜索记录）
│   │   ├── CategoryDetailTabView.kt         # 分类详情页（StartBrowsing/BrowseAll共用）
│   │   └── CodeTabView.kt                  # Spotify Code 扫描页
│   ├── library/
│   │   ├── YourLibraryTabView.kt            # 音乐库主页面（含子页面导航）
│   │   ├── CreateTabView.kt                 # 创建弹窗（Playlist/Blend 选项）
│   │   ├── PlaylistNameTabView.kt           # 歌单命名页面（创建新歌单时输入名称）
│   │   ├── AddToPlaylistTabView.kt          # 添加歌曲到歌单页面（搜索+添加）
│   │   ├── SearchYourLibraryTabView.kt      # 搜索音乐库（搜索框 + 最近搜索）
│   │   ├── LikedSongsTabView.kt             # 喜欢的歌曲详情页
│   │   ├── ArtistTabView.kt                 # 艺术家详情页（热门歌曲 + 唱片集）
│   │   ├── AddArtistsTabView.kt             # 添加艺术家页面（推荐 + Follow）
│   │   ├── AddEventsAndVenuesTabView.kt     # 添加活动和场馆页面
│   │   ├── AddPodcastsTabView.kt            # 添加播客页面
│   │   └── ImportYourMusicTabView.kt        # 导入音乐页面
│   ├── premium/
│   │   ├── PremiumTabView.kt            # Premium 订阅推广页面
│   │   └── CheckOutTabView.kt           # Premium 结算页面
│   └── theme/
│       ├── Color.kt
│       ├── Theme.kt
│       └── Type.kt
└── MainActivity.kt     # 主入口

app/src/main/assets/
├── data/               # 初始数据 (JSON)
│   ├── songs.json
│   ├── artists.json
│   ├── albums.json
│   ├── playlists.json
│   ├── user_data.json
│   └── playback_state.json
├── avatar/             # 用户/歌手头像 (1.png - 10.png)
├── cover/              # 专辑/歌单封面 (1.png - 15.png)
├── video_cover/        # 播客视频封面 (1.jpg - 3.jpg)
└── audiobook_cover/    # 有声书封面 (1.png - 3.png)
```

## 页面结构

```
MainActivity
├── HomeTab (All)
│   ├── UserTab          ← 点击头像进入，含个人信息和菜单
│   ├── MusicTab         ← 筛选标签 "Music"，与 All 内容相同但板块顺序不同
│   ├── PodcastsTab      ← 筛选标签 "Podcasts"，播客分类、热门节目、推荐单集
│   │   └── PodcastDetail   ← 点击播客卡片进入，视频预览+进度条+播放控制+评论区
│   └── AudiobooksTab    ← 筛选标签 "Audiobooks"，有声书分类、热门书籍、新发布
│       └── AudiobookDetail ← 点击有声书卡片进入，封面+播放控制+书籍介绍
├── SearchTab
│   ├── SearchForSomethingTab  ← 点击搜索框进入，显示最近搜索记录，可删除
│   ├── StartBrowsingTab       ← 点击 Start browsing 分类卡片进入，显示推荐内容
│   ├── BrowseAllTab           ← 点击 Browse all 分类卡片进入，显示推荐内容
│   └── CodeTab                ← 点击右上角相机图标进入，Spotify Code 扫描页
├── YourLibraryTab
│   ├── CreateTab             ← 点击右上角 "+" 进入，底部弹窗可选 Playlist/Blend
│   │   ├── PlaylistNameTab  ← 选择 Playlist 后进入，输入歌单名称
│   │   ├── NullPlaylist     ← 创建后进入空歌单详情页（音乐图标封面）
│   │   └── AddToPlaylistTab ← 点击 "Add to this playlist" 进入，搜索并添加歌曲
│   ├── SearchYourLibraryTab  ← 点击右上角搜索图标进入，搜索库内容，显示最近搜索
│   ├── LikedSongsTab        ← 点击 Liked Songs 进入，显示喜欢的歌曲列表
│   ├── ArtistTab            ← 点击已关注艺术家进入，显示热门歌曲和唱片集
│   ├── AddArtistsTab        ← 点击 Add artists 进入，推荐未关注的艺术家
│   ├── AddEventsAndVenuesTab ← 点击 Add events and venues 进入，推荐场馆
│   ├── AddPodcastsTab       ← 点击 Add podcasts 进入，推荐播客
│   └── ImportYourMusicTab   ← 点击 Import your music 进入，导入音乐引导页
├── PremiumTab
│   └── CheckOutTab          ← 点击 "Try 3 months for $0" 进入，结算页面
└── PlayingTab           ← 点击底部播放栏进入，全屏播放页面
    ├── MenuTab          ← 点击右上角三点进入，歌曲操作菜单
    ├── LyricsTab        ← 点击歌词预览卡片 "Show more" 进入，全屏歌词
    ├── CreditsTab       ← 点击 "Show all credits" 进入，制作人员名单
    └── AboutTheArtistTab ← 点击艺术家卡片 "Show more" 进入，艺术家介绍
Playlist_menu              ← 点击歌单详情页右上角三点按钮弹出，歌单操作菜单
Music_menu                 ← 点击歌曲行右侧三点按钮弹出，歌曲操作菜单
```

## 功能特性

- **主页 (HomeTab)**: 筛选标签切换 All/Music/Podcasts/Audiobooks 内容，点击头像进入用户页面
  - **All**: 歌曲列表、专辑推荐、Made For You、Recently played、Popular playlists
  - **Music**: 与 All 相同内容但板块顺序调换
  - **Podcasts**: 视频播客 Feed 流，每个项目含大尺寸预览图（带暂停/音量按钮）、标题、来源缩略图和发布时间
    - **PodcastDetail**: 点击播客卡片进入详情页，含视频预览大图、可拖动进度条（显示当前时间/总时长）、播放控制（后退/播放暂停/前进）、标题+来源信息、评论区（预设5条评论，支持发表新评论并实时显示）
  - **Audiobooks**: 有声书卡片 Feed 流，每个卡片含缩略图、书名、作者、内容介绍，右下角播放/音量按钮
    - **AudiobookDetail**: 点击有声书卡片进入详情页，含大尺寸封面、书名+作者+总时长、可拖动进度条、播放控制、英文书籍介绍（根据书名自动匹配不同内容）
  - **UserTab**: 用户头像、名称、View profile、菜单（Add account、What's new 等）
  - **PlayingTab**: 点击底部播放栏进入，全屏可滚动播放页面，含大封面、歌曲信息、进度条、播放控制（Shuffle/上一首/播放暂停/下一首/Repeat三态切换）、队列菜单和分享按钮、Lyrics preview 歌词预览卡片、About the artist 艺术家介绍卡片、Credits 制作信息卡片
    - **MenuTab**: 点击右上角三点按钮进入，歌曲操作菜单（Like、Hide、Add to playlist、Add to queue、View album/artist、Share、Song radio、Song credits、Sleep timer），显示当前播放歌曲信息
      - **SelectPlaylist**: 点击 "Add to playlist" 进入，搜索并选择歌单（含 Liked Songs + 用户创建歌单），选择后点击 Done 添加并弹出 "Added to [歌单名]" 提示
      - **SleepTimer**: 点击 "Sleep timer" 进入，选择定时时长（5/10/15/30/45分钟、1小时、曲末），设置后图标变绿并显示时长，再次进入可关闭定时器
    - **LyricsTab**: 点击歌词预览卡片进入，渐变色背景全屏歌词展示，当前行高亮
    - **CreditsTab**: 点击 "Show all credits" 进入，歌曲制作人员分工名单
    - **AboutTheArtistTab**: 点击艺术家卡片进入，大图、月听众数、英文介绍、Follow 按钮
- **搜索 (SearchTab)**: 搜索栏、Start browsing/Discover something new/Browse all 三板块
  - **SearchForSomethingTab**: 点击搜索框进入，显示最近搜索记录（歌曲+歌手），支持删除
  - **CategoryDetailTab**: 点击分类卡片进入，展示封面+标题的推荐内容网格，根据分类自动匹配数据
  - **CodeTab**: 右上角相机图标进入，Spotify Code 扫描页面（静态UI）
- **音乐库 (YourLibraryTab)**: 播放列表、艺术家（含真实头像）、喜欢的歌曲
  - **CreateTab**: 点击右上角 "+" 弹出底部弹窗，提供 Playlist 和 Blend 两个创建选项
    - **PlaylistNameTab**: 选择 Playlist 后进入命名页面，输入歌单名称，Cancel 取消，Create 创建（名称非空时按钮变绿）
    - **NullPlaylist**: 创建后进入空歌单详情页，显示音乐图标封面，"Add to this playlist" 入口
    - **AddToPlaylistTab**: 搜索歌曲并添加到歌单，支持按歌名/歌手搜索，点击 "+" 添加后底部弹出 "Added to [歌单名]" 提示，已添加的歌曲显示绿色 ✓
  - **SearchYourLibraryTab**: 点击右上角搜索图标进入，搜索框 + 最近搜索记录（已关注艺术家）
  - **LikedSongsTab**: 点击 Liked Songs 进入，渐变色头图、歌曲数量、Shuffle/Play 按钮、歌曲列表
  - **ArtistTab**: 点击已关注艺术家进入，大头像、月听众数、热门歌曲列表（带序号和播放量）、Discography 横向专辑卡片
  - **AddArtistsTab**: 点击 Add artists 进入，搜索框 + 推荐未关注艺术家列表（含 Follow 按钮）
  - **AddEventsAndVenuesTab**: 点击 Add events and venues 进入，搜索框 + 推荐场馆列表（含 Follow 按钮）
  - **AddPodcastsTab**: 点击 Add podcasts 进入，搜索框 + 推荐播客列表（含 Follow 按钮）
  - **ImportYourMusicTab**: 点击 Import your music 进入，导入说明 + Import 按钮 + Learn more
- **Premium (PremiumTab)**: Premium 订阅页面
  - **CheckOutTab**: 点击 "Try 3 months for $0" 进入，显示 Premium Individual 套餐信息、支付方式选择、Activate Premium 按钮和条款说明
- **歌单菜单 (Playlist_menu)**: 点击歌单/专辑详情页右上角三点按钮弹出，全屏菜单页面，显示歌单封面和名称，提供 Add to Your Library、Download、Add to queue、Share、Go to radio、Report 操作
- **歌曲菜单 (Music_menu)**: 点击歌曲行右侧三点按钮弹出，全屏菜单页面，显示歌曲封面和信息，提供 Like、Hide this song、Add to playlist、Add to queue、View album、View artist、Share、Song credits、Sleep timer、Go to radio、Report 操作
- **歌曲收藏功能**: 所有歌曲列表项（首页、歌单详情、艺术家详情、喜欢的歌曲）和全屏播放页面均支持收藏/取消收藏，"+"图标表示未收藏，绿色"✓"表示已收藏，收藏的歌曲自动加入 Liked Songs 歌单

## 技术栈

- Kotlin 1.9.25
- Jetpack Compose
- Material Design 3
- Gson (JSON 解析)
- 图片加载：使用 Android 原生 BitmapFactory 从 assets 加载，无需第三方库

## 构建说明

```bash
./gradlew assembleDebug
```

构建成功后可在 Android 设备或模拟器上运行。
