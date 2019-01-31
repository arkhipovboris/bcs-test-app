package bcs.model;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
public class QuotesResponse {

	@Setter(AccessLevel.NONE)
	private BigDecimal value;
	@Setter(AccessLevel.NONE)
	private List<Allocation> allocations = new ArrayList<>();

	public void addAllocations(List<Allocation> allocations) {
		this.allocations.addAll(allocations);
		recount();
	}

	public void addAllocation(Allocation allocation) {
		this.allocations.add(allocation);
		recount();
	}

	public void removeAllocations(List<Allocation> allocations) {
		this.allocations.removeAll(allocations);
		recount();
	}

	public void removeAllocation(Allocation allocation) {
		this.allocations.remove(allocation);
		recount();
	}

	private void recount() {
		this.value = this.allocations.stream()
				.map(Allocation::getAssetValue)
				.reduce(BigDecimal.ZERO, BigDecimal::add);
		this.allocations.forEach(allocation ->
				allocation.setProportion(allocation.getAssetValue().divide(this.value, BigDecimal.ROUND_HALF_DOWN)));
	}

	@Data
	@NoArgsConstructor
	public static class Allocation {

		private String sector;
		private BigDecimal assetValue;
		private BigDecimal proportion;

		public Allocation(String sector, BigDecimal assetValue) {
			this.sector = sector;
			this.assetValue = assetValue.setScale(2, BigDecimal.ROUND_HALF_DOWN);
		}
	}
}
