package uwu.lopyluna.create_bnz.content.items.zapper.tools;

public enum TerrainBrushes {
	
	Cuboid(new CuboidBrush()),
	Sphere(new SphereBrush()),
	Cylinder(new CylinderBrush()),
	Surface(new DynamicBrush(true)),
	Cluster(new DynamicBrush(false)),
	
	;
	
	private final Brush brush;

	TerrainBrushes(Brush brush) {
		this.brush = brush;
	}

	public Brush get() {
		return brush;
	}

}
