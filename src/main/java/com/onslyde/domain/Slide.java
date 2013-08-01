package com.onslyde.domain;

// Generated Jun 15, 2012 8:41:06 AM by Hibernate Tools 3.4.0.CR1

import org.codehaus.jackson.annotate.JsonIgnore;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

import static javax.persistence.GenerationType.IDENTITY;

/**
 * Slide generated by hbm2java
 */
@Entity
@Table(name = "slide", catalog = "onslyde")
public class Slide implements java.io.Serializable {

	private Integer id;
	private SlideGroup slideGroup;
	private String name;
	private String slideIndex;
    private Set<SlideVotes> slideVoteses = new HashSet<SlideVotes>(
            0);
    private Set<SlideOptions> slideOptionses = new HashSet<SlideOptions>(
            0);

	public Slide() {
	}

	public Slide(SlideGroup slideGroup, String slideIndex) {
		this.slideGroup = slideGroup;
		this.slideIndex = slideIndex;
	}

	public Slide(SlideGroup slideGroup, String name, String slideIndex,
                 Set<SlideVotes> slideVoteses,
                 Set<SlideOptions> slideOptionses) {
        this.slideGroup = slideGroup;
		this.name = name;
		this.slideIndex = slideIndex;
        this.slideVoteses = slideVoteses;
        this.slideOptionses = slideOptionses;
	}

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

    @JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "slide_group_id", nullable = false)
	public SlideGroup getSlideGroup() {
		return this.slideGroup;
	}

	public void setSlideGroup(SlideGroup slideGroup) {
		this.slideGroup = slideGroup;
	}

	@Column(name = "name")
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "slide_index", nullable = false)
	public String getSlideIndex() {
		return this.slideIndex;
	}

	public void setSlideIndex(String slideIndex) {
		this.slideIndex = slideIndex;
	}

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "slide")
    @OrderBy("voteTime ASC")
    public Set<SlideVotes> getSlideVoteses() {
        return this.slideVoteses;
    }

    public void setSlideVoteses(Set<SlideVotes> slideVoteses) {
        this.slideVoteses = slideVoteses;
    }

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "slide")
    public Set<SlideOptions> getSlideOptionses() {
        return this.slideOptionses;
    }

    public void setSlideOptionses(
            Set<SlideOptions> slideOptionses) {
        this.slideOptionses = slideOptionses;
    }

}
