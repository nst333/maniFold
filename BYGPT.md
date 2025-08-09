# BYGPT — maniFold

This repo was redesigned and scaffolded with ChatGPT’s assistance for **maniFold** (Harmonized Aid for Creative Kinesis).  

## What ChatGPT did
- **Renamed & normalized**: `Wurst` → `maniFold` branding, Fabric namespace → `manifold` (lowercase).
- **New design system**: Flutter-like, componentized UI kit with a **LiquidGlass** vibe (dreamy purple).
  - Theme & draw utils
  - Reusable widgets: Button, TextField, Switch, Slider, Card
  - Layout primitives: Column (Row can be added in the same pattern)
- **Screens**
  - **QuickSearch (RShift)**: minimal, centered search with instant toggle, keyboard-first
  - **Hack List (RCtrl)**: category-driven list, airy layout, discoverable
- **Convenience feature**: `BridgeAssist` (singleplayer bridging helper)
- **Assets & IDs**: moved to `assets/manifold/**`, all Identifiers use "manifold" namespace
- **ClickGUI**: opens expanded by default; updated accent to mystical purple

## Design Principles
- Apple-like simplicity: generous whitespace, quiet chrome, focused surfaces
- LiquidGlass visuals: translucent panels, subtle edge highlights, soft contrast
- Keyboard-first UX: ↑/↓ navigate, Enter/Space toggle, Esc close; mouse stays friendly
- Clean architecture: small, composable widgets over one big monolith

## Dev Quickstart
```bash
./gradlew clean
./gradlew runClient --stacktrace
```

## Contributing
- Keep UI components small & reusable (prefer composition over inheritance).
- Namespace: manifold (lowercase) for Fabric resources & Identifiers.
- Commit style: feat:, fix:, docs:, refactor:, style:, chore:, test:.

---

*Built with ❤️ for creators. If it feels like Wurst, we didn’t go far enough.*
