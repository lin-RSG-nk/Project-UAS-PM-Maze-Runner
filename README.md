# 2D Pixel Art Maze Runner Game

Game 2D pixel-art Maze Runner yang dibuat dengan Java Swing. Pemain harus melewati 3 level maze yang semakin sulit untuk mencapai tujuan.

## Spesifikasi Teknis

- **Platform**: Java (Java 11+)
- **GUI Framework**: Java Swing
- **Resolusi**: 1280 × 720 px
- **Grid**: 40 kolom × 24 baris
- **Tile Size**: 32×32 px (16×16 original dengan scale 2x)

## Fitur Game

### Gameplay
- ✅ 3 level maze dengan tingkat kesulitan meningkat
- ✅ Kontrol 4 arah (WASD atau Arrow Keys)
- ✅ Collision detection dengan dinding
- ✅ Start position (S) dan Goal position (G) di setiap level
- ✅ Pesan "Level Complete!" saat mencapai goal
- ✅ Auto-transition ke level berikutnya
- ✅ Layar kemenangan setelah menyelesaikan semua level

### Menu System
- ✅ Main Menu dengan pilihan: Level, Setting, Exit
- ✅ Level Selection Menu untuk memilih level 1, 2, atau 3
- ✅ Setting Menu (placeholder)
- ✅ Game Over/Victory Screen

### Maze Design
- **Level 1**: Maze rumit namun dapat dipelajari dengan banyak kelokan dan beberapa dead-end
- **Level 2**: Lebih kompleks dengan banyak false path dan dead-end
- **Level 3**: Paling sulit, sangat padat dan berliku dengan banyak cabang

## Struktur Project

```
Project-UAS-PM-Maze-Runner1/
├── src/
│   ├── ENTITY/
│   │   ├── Entity.java          # Base class untuk entity
│   │   └── Player.java          # Player class dengan movement dan collision
│   ├── LEVEL/
│   │   ├── levelManager.java    # Manager untuk loading dan rendering level
│   │   ├── Level1.java          # (tidak digunakan)
│   │   └── Tingkatan.java       # Tile class
│   └── My2DMazeRunner/
│       ├── Main.java            # Entry point
│       ├── GamePanel.java       # Main game panel dengan game loop
│       └── KeyHandler.java     # Input handler untuk keyboard
├── Resource/
│   ├── Map/
│   │   ├── Maplvl1.txt         # Level 1 map (format: #, ., S, G)
│   │   ├── Maplvl2.txt         # Level 2 map
│   │   └── Maplvl3.txt         # Level 3 map
│   ├── Player/
│   │   ├── boy_*.png           # Player sprites
│   │   └── MainMenu.jpg        # Menu background
│   └── Tiles/
│       ├── earth.png           # Path tile
│       └── wall.png            # Wall tile
└── README.md
```

## Format File Level

File level menggunakan format teks dengan karakter berikut:
- `#` = Wall (dinding) - menggunakan wall.png
- `.` = Path (jalan) - menggunakan earth.png
- `S` = Start position (posisi awal player)
- `G` = Goal position (tujuan/finish)

Setiap baris harus tepat 40 karakter, dan total 24 baris per level.

### Contoh Format:
```
########################################
#S..#......#.....#......#....#......#.#
###.#.####.#.###.#.###.###.##.#.##.#.###
...
#...#...........#.........#.....#..G.#
########################################
```

## Cara Build dan Run

### Prerequisites
- Java JDK 11 atau lebih tinggi
- IDE (IntelliJ IDEA, Eclipse, atau VS Code dengan Java extension)

### Build Manual
1. Clone atau download project ini
2. Buka project di IDE favorit Anda
3. Pastikan semua file resource ada di folder `Resource/`
4. Compile semua file Java:
   ```bash
   javac -d out -sourcepath src src/My2DMazeRunner/Main.java
   ```
5. Run game:
   ```bash
   java -cp out My2DMazeRunner.Main
   ```

### Run dari IDE
1. Buka project di IDE
2. Set working directory ke root project
3. Run `Main.java` dari package `My2DMazeRunner`

## Kontrol Game

- **W / ↑**: Move Up
- **S / ↓**: Move Down
- **A / ←**: Move Left
- **D / →**: Move Right
- **ESC**: Kembali ke menu utama (saat bermain)
- **ENTER**: Pilih menu / Konfirmasi
- **UP/DOWN**: Navigasi menu

## Cara Bermain

1. **Main Menu**: Pilih "Level" untuk memilih level
2. **Level Selection**: Pilih Level 1, 2, atau 3
3. **Gameplay**: 
   - Player mulai di posisi S (Start)
   - Gunakan WASD atau Arrow Keys untuk bergerak
   - Hindari dinding (abu-abu)
   - Capai posisi G (Goal) untuk menyelesaikan level
4. **Level Complete**: Setelah mencapai goal, akan muncul pesan "Level Complete!" selama 2 detik
5. **Next Level**: Game otomatis pindah ke level berikutnya
6. **Victory**: Setelah menyelesaikan Level 3, akan muncul layar kemenangan

## Arsitektur Kode

### GamePanel.java
- Main game panel yang mengatur game loop, state management, dan rendering
- Menggunakan `Runnable` interface untuk game loop dengan 60 FPS
- State machine: MENU_STATE, LEVEL_SELECTION_STATE, PLAYING_STATE, LEVEL_COMPLETE_STATE, GAME_OVER_STATE

### levelManager.java
- Load map dari file .txt dengan format karakter (#, ., S, G)
- Convert karakter ke tile number (0=path, 1=wall, 2=goal)
- Render tiles dengan earth.png dan wall.png
- Menyimpan posisi start (S) dan goal (G)
- Portal effect untuk goal position

### Player.java
- Entity class yang menangani movement dan collision
- Collision detection dengan 4 corner check
- Sprite animation dengan 2 frame per direction
- Start position diambil dari levelManager

### KeyHandler.java
- Input handler untuk keyboard
- Debouncing untuk mencegah input spam
- Support WASD dan Arrow Keys

## Customization

### Mengubah Tile Size
Edit di `GamePanel.java`:
```java
final int originalTileSize = 16;  // Ubah ukuran tile original
final int scale = 2;              // Ubah scale factor
```

### Menambahkan Level Baru
1. Buat file map baru di `Resource/Map/Maplvl4.txt` dengan format yang sama
2. Tambahkan case di `levelManager.java`:
```java
case 4:
    mapFile = "/Map/Maplvl4.txt";
    break;
```
3. Tambahkan opsi di level selection menu di `GamePanel.java`

### Mengganti Asset
- Ganti file PNG di folder `Resource/Tiles/` untuk tiles
- Ganti file PNG di folder `Resource/Player/` untuk player sprites
- Pastikan nama file tetap sama atau update path di kode

## Testing

### Manual Testing Checklist
- [x] Player dapat bergerak ke 4 arah
- [x] Collision dengan dinding bekerja
- [x] Player start di posisi S
- [x] Goal detection bekerja di posisi G
- [x] Pesan "Level Complete!" muncul
- [x] Auto-transition ke level berikutnya
- [x] Victory screen muncul setelah Level 3
- [x] Menu navigation bekerja
- [x] ESC kembali ke menu

### Pathfinding Test
Setiap level telah divalidasi bahwa ada jalur dari S ke G menggunakan pathfinding algorithm.

## Known Issues / Limitations

1. **JavaFX tidak digunakan**: Menggunakan Swing karena lebih sederhana dan tidak memerlukan module system
2. **Grid-based movement**: Movement smooth tapi tetap mematuhi tile collision
3. **No procedural generation**: Level dibuat manual untuk memastikan kualitas dan solvability

## Future Improvements

- [ ] Procedural maze generation dengan seed
- [ ] Timer dan step counter
- [ ] Sound effects dan background music
- [ ] Save/Load game state
- [ ] Mini-map
- [ ] Multiple difficulty modes
- [ ] Leaderboard

## Credits

- Game dibuat untuk UAS Pemrograman Mobile
- Assets: earth.png, wall.png, player sprites
- Maze design: Manual creation dengan fokus pada complexity dan solvability

## License

Project ini dibuat untuk tujuan edukasi.

---

**Selamat Bermain!** 🎮


