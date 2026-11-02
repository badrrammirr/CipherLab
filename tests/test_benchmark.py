"""
Tests for benchmark module
"""

import pytest
from benchmark.performance import benchmark_algorithm, benchmark_all, BenchmarkResult


class TestBenchmark:
    """Test performance benchmarking"""
    
    def test_benchmark_result_creation(self):
        result = BenchmarkResult("Test Algorithm", 1.5, 2.0)
        assert result.algorithm == "Test Algorithm"
        assert result.encrypt_time == 1.5
        assert result.decrypt_time == 2.0
    
    def test_benchmark_caesar(self):
        input_text = "Hello World"
        result = benchmark_algorithm("Caesar Cipher", input_text, 1)
        assert result.algorithm == "Caesar Cipher"
        assert result.encrypt_time >= 0
        assert result.decrypt_time >= 0
    
    def test_benchmark_affine(self):
        input_text = "Hello World"
        result = benchmark_algorithm("Affine Cipher", input_text, 1)
        assert result.algorithm == "Affine Cipher"
        assert result.encrypt_time >= 0
    
    def test_benchmark_sha256(self):
        input_text = "Hello World"
        result = benchmark_algorithm("SHA-256", input_text, 1)
        assert result.algorithm == "SHA-256"
        assert result.encrypt_time >= 0
        assert result.decrypt_time == -1  # SHA-256 doesn't decrypt
    
    def test_benchmark_all(self):
        input_text = "Test"
        results = benchmark_all(input_text, 1)
        assert len(results) == 9  # 9 algorithms
        assert all(isinstance(r, BenchmarkResult) for r in results)
